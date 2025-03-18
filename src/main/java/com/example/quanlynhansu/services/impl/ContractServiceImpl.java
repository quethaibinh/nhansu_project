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
import com.example.quanlynhansu.services.WorkHistoryService;
import com.example.quanlynhansu.services.securityService.InfoCurrentUserService;
import com.example.quanlynhansu.utils.CheckTime;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private CheckTime checkTime;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Transactional
    @Override
    public ContractResponse addContract(ContractRequest contractRequest) throws ParseException {
        ContractEntity contractEntity = contractConverterRequestEntity.requestToEntity(contractRequest);

        if(contractRequest.getId() == null){ // tạo hợp đồng mới, gia hạn hợp đồng, ..., TH update hợp đồng (do sai thông tin, ...) thì chạy xuống dưới
            EmployeeEntity employeeEntity = employeeRepo.findOneById(contractRequest.getEmployeeId());
            if(employeeEntity != null){
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
            }
            else{
                return null; // thay bằng exception thì hay hơn
            }
        } // chấm dứt hợp đồng trước thời hạn được cập nhật ở workhistory trong api của workhistory

        contractRepo.save(contractEntity);
        return contractConverterRequestEntity.entityToResponse(contractEntity);

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

    @Override // id gửi bằng pathVariable, còn lại gửi theo dạng body
    public ContractResponse updateContract(ContractRequest contractRequest) throws ParseException {

        ContractEntity contractEntity = contractRepo.findOneById(contractRequest.getId());
        if(contractEntity != null){
            // update trong bảng work history nếu có
            if(contractEntity.getWorkHistory() != null){
                // không dùng converter vì sẽ dễ xảy ra xung đột multi entity, nếu khai báo 2 biến cùng 1 dạng entity thì khi save, hibernate sẽ không biết lưu entity nào.
                contractEntity.setContractType(contractRequest.getContractType());
                contractEntity.setDepartment(contractRequest.getDepartment());
                contractEntity.setDocumentUrl(contractRequest.getDocumentUrl());
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
