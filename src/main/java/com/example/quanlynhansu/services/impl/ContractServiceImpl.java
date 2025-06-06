package com.example.quanlynhansu.services.impl;

import com.example.quanlynhansu.converters.ContractConverterRequestEntity;
import com.example.quanlynhansu.converters.WorkHistoryConverterResponseEntity;
import com.example.quanlynhansu.models.entity.ContractEntity;
import com.example.quanlynhansu.models.entity.EmployeeEntity;
import com.example.quanlynhansu.models.entity.WorkHistoryEntity;
import com.example.quanlynhansu.models.request.contract.ContractRequest;
import com.example.quanlynhansu.models.request.workHistory.WorkHistoryRequest;
import com.example.quanlynhansu.models.response.ContractResponse;
import com.example.quanlynhansu.repos.ContractRepo;
import com.example.quanlynhansu.repos.EmployeeRepo;
import com.example.quanlynhansu.repos.WorkHistoryRepo;
import com.example.quanlynhansu.services.ContractService;
import com.example.quanlynhansu.services.EmailService;
import com.example.quanlynhansu.services.MinioService;
import com.example.quanlynhansu.services.WorkHistoryService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import com.example.quanlynhansu.utils.CheckTime;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private WorkHistoryRepo workHistoryRepo;

    @Autowired
    private ContractConverterRequestEntity contractConverterRequestEntity;

    @Autowired
    private WorkHistoryConverterResponseEntity workHistoryConverterResponseEntity;

    @Autowired
    private WorkHistoryService workHistoryService;

    @Autowired
    private InfoCurrentUserService infoCurrentUserService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private CheckTime checkTime;

    @Autowired
    private EmailService emailService;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Transactional
    @Override
    public ResponseEntity<?> addContract(ContractRequest contractRequest, MultipartFile file) throws ParseException, MessagingException {
        ContractEntity contractEntity = contractConverterRequestEntity.requestToEntity(contractRequest);

        try{

            // Upload file lên MinIO và lấy URL
            String fileUrl = minioService.uploadFile(file);
            contractEntity.setDocumentUrl(fileUrl);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

        if(contractRequest.getId() == null){ // tạo hợp đồng mới, gia hạn hợp đồng, ..., TH update hợp đồng (do sai thông tin, ...) thì chạy xuống dưới
            try{
                EmployeeEntity employeeEntity = employeeRepo.findOneById(contractRequest.getEmployeeId());
                List<WorkHistoryEntity> listWork = employeeEntity.getWorkHistory();
                if(listWork != null){ // đã từng là nhân viên và bây giờ gia hạn hợp đồng
                    for(WorkHistoryEntity workHistory : listWork){ // kiểm tra xem có còn đang trong thời gian làm việc của hợp đồng cũ không
                        if(workHistory.getEndDate().after(contractEntity.getStartDate())){ // nếu có thì kết thúc => update endDate
//                            WorkHistoryRequest workHistoryRequest = workHistoryConverterResponseEntity.entityToRequest(workHistory);
//                            workHistoryRequest.setEndDate(contractRequest.getStartDate()); // thời gian kết thúc chính là thời gian bắt đầu của hợp đồng mới
//                            workHistoryService.addOrUpdateWorkHistory(workHistoryRequest);
                            // nếu dùng hàm addOrUpdateWorkHistory như trên thì sẽ bị lỗi do đã gọi workHistory từ db rồi mà trong hàm addOrUpdate lại save 1 class khác, nó bị lỗi multi khi khai báo nhiều entity và hibernate không biết phải save entity nào.
                            workHistory.setEndDate(sdf.parse(contractRequest.getStartDate()));
                            workHistoryRepo.save(workHistory);
                        }
                    }
                }
                employeeEntity.setPosition(contractEntity.getContractType()); // tạo hợp đồng hoặc gia hạn hợp đồng thì sẽ update position

                // tiếp tục tạo lịch sử làm việc mới
                WorkHistoryEntity workHistoryEntity = new WorkHistoryEntity();
                workHistoryEntity.setDepartment(contractEntity.getDepartment());
                workHistoryEntity.setPosition(contractEntity.getContractType());
                workHistoryEntity.setStartDate(contractEntity.getStartDate());
                workHistoryEntity.setEndDate(contractEntity.getEndDate());

                listWork.add(workHistoryEntity);
                employeeEntity.setWorkHistory(listWork);
                contractEntity.setWorkHistory(workHistoryEntity);
                workHistoryEntity.setEmployee(employeeEntity);
                workHistoryEntity.setContract(contractEntity);

            }catch(Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(e.getMessage());
            }
        } // chấm dứt hợp đồng trước thời hạn được cập nhật ở workhistory trong api của workhistory

        contractRepo.save(contractEntity);
        // HTML nội dung email
        String html = "<h2>Thông báo từ phòng nhân sự</h2>" +
                "<p>Bạn đã đươc thêm hợp đồng mới.</p>" +
                "<p>Vui lòng truy cập web để xem chi tiết!</p>";

        emailService.sendHtmlEmailWithAttachment(
                contractEntity.getEmployee().getEmail(),
                "Thông báo tài khoản TYP",
                html,
                null
        );

        return ResponseEntity.ok(contractConverterRequestEntity.entityToResponse(contractEntity));

    }

    @Override
    public List<ContractResponse> selectContract() {

        String username = infoCurrentUserService.getCurrentUsername();
        EmployeeEntity employeeEntity = employeeRepo.findOneByEmail(username);

        if(employeeEntity != null){
            List<ContractResponse> responses = new ArrayList<>();
            for(ContractEntity contract: employeeEntity.getContracts()){
                responses.add(contractConverterRequestEntity.entityToResponse(contract));
            }
            return responses;
        }

        return null;
    }

    @Override // dành cho admin và HR
    public List<ContractResponse> findById(Long id) {

        List<ContractEntity> list = contractRepo.findAllByEmployee_Id(id);
        if(list != null){
            List<ContractResponse> responses = new ArrayList<>();
            for(ContractEntity contract: list){
                responses.add(contractConverterRequestEntity.entityToResponse(contract));
            }
            return responses;
        }

        return null;
    }

    // hàm này chưa được sử dụng, nếu sử dụng thì phải fix lại xíu
    @Override // id gửi bằng pathVariable, còn lại gửi theo dạng body
    public ContractResponse updateContract(ContractRequest contractRequest) throws ParseException {

        ContractEntity contractEntity = contractRepo.findOneById(contractRequest.getId());
        if(contractEntity != null){
            // update trong bảng work history nếu có
            if(contractEntity.getWorkHistory() != null){
                // không dùng converter vì sẽ dễ xảy ra xung đột multi entity, nếu khai báo 2 biến cùng 1 dạng entity thì khi save, hibernate sẽ không biết lưu entity nào.
                contractEntity.setContractType(contractRequest.getContractType());
                contractEntity.setDepartment(contractRequest.getDepartment());
                contractEntity.setSalary(contractRequest.getSalary());
                contractEntity.setStartDate(sdf.parse(contractRequest.getStartDate()));
                contractEntity.setEndDate(sdf.parse(contractRequest.getEndDate()));

                contractEntity.getWorkHistory().setDepartment(contractEntity.getDepartment());
                contractEntity.getWorkHistory().setPosition(contractEntity.getContractType());
                contractEntity.getWorkHistory().setStartDate(contractEntity.getStartDate());
                contractEntity.getWorkHistory().setEndDate(contractEntity.getEndDate());

                contractRepo.save(contractEntity);
                return contractConverterRequestEntity.entityToResponse(contractEntity);
            }
        }

        return null;
    }

}
