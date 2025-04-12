$(document).ready(function() {
    // Toggle action menu
    $('#action_menu_btn').click(function() {
        $('.action_menu').toggle();
    });

    // Hide action menu when clicking outside
    $(document).click(function(e) {
        if (!$(e.target).closest('#action_menu_btn, .action_menu').length) {
            $('.action_menu').hide();
        }
    });

    // Auto-resize textarea height based on content
    $('.type_msg').on('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
        if (this.scrollHeight > 100) {
            this.style.height = '100px';
        }
    });

    // Send message on Enter key (Shift+Enter for new line)
    $('.type_msg').keypress(function(e) {
        if (e.which === 13 && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    // Send message on click send button
    $('.send_btn').click(function() {
        sendMessage();
    });

    // Contact list item click handler
    $('.contacts li').click(function() {
        $('.contacts li').removeClass('active');
        $(this).addClass('active');
        
        // Update chat header with selected contact info
        let userName = $(this).find('.user_info span').text();
        let userImg = $(this).find('.img_cont img').attr('src');
        let isOnline = $(this).find('.online_icon').hasClass('offline') ? false : true;
        
        updateChatHeader(userName, userImg, isOnline);
        
        // Clear chat messages (in a real app, you would load messages for this contact)
        clearChat();
    });

    // Open modal and scroll to the latest message
    $('#chatModal').on('shown.bs.modal', function() {
        scrollToBottom();
    });

    // Adjust height for mobile devices
    adjustHeightForMobile();
    $(window).resize(function() {
        adjustHeightForMobile();
    });

    // Hiệu ứng khi cuộn trang
    $(window).scroll(function() {
        if ($(this).scrollTop() > 200) {
            $('.chat-toggle-btn').addClass('chat-toggle-btn-visible');
        } else {
            $('.chat-toggle-btn').removeClass('chat-toggle-btn-visible');
        }
    });
    
    // Hiệu ứng khi click vào nút chat
    $('.chat-toggle-btn').click(function() {
        $(this).addClass('chat-toggle-btn-clicked');
        setTimeout(function() {
            $('.chat-toggle-btn').removeClass('chat-toggle-btn-clicked');
        }, 300);
    });

    // Function to send a message
    function sendMessage() {
        let message = $('.type_msg').val().trim();
        if (message !== '') {
            // Get current time
            let now = new Date();
            let timeStr = now.getHours() + ':' + (now.getMinutes() < 10 ? '0' : '') + now.getMinutes();
            
            // Create new message element (this would be replaced with a template in a real app)
            let newMsg = `
                <div class="d-flex justify-content-end mb-4">
                    <div class="msg_cotainer_send">
                        ${message}
                        <span class="msg_time_send">${timeStr}, Today</span>
                    </div>
                    <div class="img_cont_msg">
                        <img src="https://avatars.hsoubcdn.com/ed57f9e6329993084a436b89498b6088?s=256" class="rounded-circle user_img_msg">
                    </div>
                </div>
            `;
            
            // Append to chat body
            $('.msg_card_body').append(newMsg);
            
            // Clear input
            $('.type_msg').val('');
            
            // Reset textarea height
            $('.type_msg').css('height', 'auto');
            
            // Scroll to bottom of chat
            scrollToBottom();
            
            // Simulate reply after delay (for demo purposes only)
            setTimeout(simulateReply, 1000);
        }
    }

    // Function to simulate a reply (for demo purposes)
    function simulateReply() {
        // Get current time
        let now = new Date();
        let timeStr = now.getHours() + ':' + (now.getMinutes() < 10 ? '0' : '') + now.getMinutes();
        
        // Get active contact
        let activeContact = $('.contacts li.active');
        let userImg = activeContact.find('.img_cont img').attr('src');
        
        // Array of possible replies
        const replies = [
            "Tôi hiểu rồi!",
            "Cảm ơn bạn đã chia sẻ.",
            "Tôi sẽ xem xét vấn đề này.",
            "Chúng ta sẽ thảo luận thêm sau nhé.",
            "Thông tin rất hữu ích."
        ];
        
        // Select a random reply
        const randomReply = replies[Math.floor(Math.random() * replies.length)];
        
        // Create reply message element
        let replyMsg = `
            <div class="d-flex justify-content-start mb-4">
                <div class="img_cont_msg">
                    <img src="${userImg}" class="rounded-circle user_img_msg">
                </div>
                <div class="msg_cotainer">
                    ${randomReply}
                    <span class="msg_time">${timeStr}, Today</span>
                </div>
            </div>
        `;
        
        // Append to chat body
        $('.msg_card_body').append(replyMsg);
        
        // Scroll to bottom of chat
        scrollToBottom();
    }

    // Function to update chat header
    function updateChatHeader(name, img, online) {
        $('.msg_head .user_info span').text(name);
        $('.msg_head .img_cont img').attr('src', img);
        
        if (online) {
            $('.msg_head .online_icon').removeClass('offline');
        } else {
            $('.msg_head .online_icon').addClass('offline');
        }
    }

    // Function to clear chat messages
    function clearChat() {
        $('.msg_card_body').empty();
    }

    // Function to scroll to bottom of chat
    function scrollToBottom() {
        let msgBody = $('.msg_card_body');
        msgBody.scrollTop(msgBody[0].scrollHeight);
    }

    // Adjust height for mobile devices
    function adjustHeightForMobile() {
        if ($(window).width() <= 768) {
            $('.modal-body').css('max-height', ($(window).height() * 0.7) + 'px');
            $('.msg_card_body').css('max-height', ($(window).height() * 0.4) + 'px');
            $('.contacts_body').css('max-height', ($(window).height() * 0.4) + 'px');
        } else {
            $('.modal-body').css('max-height', '');
            $('.msg_card_body').css('height', '400px');
            $('.contacts_body').css('height', '400px');
        }
    }
});