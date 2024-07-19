function showSwal(title, type) {
    swal({
        title: title,
        icon: type === 'success' ? 'success' : 'error',
        timer: 3000,
        buttons: true,
        type: type
    });
}
function reloadPage() {
    setTimeout(function(){
        window.location.reload();
    }, 500);
}
//var buttonState = localStorage.getItem('buttonState');
//
//if (buttonState === 'followed') {
//    $('#followButton').html('<span class="icon-heart mr-2 text-danger"></span>Đã theo dõi');
//} else {
//    $('#followButton').html('<span class="icon-heart-o mr-2 text-danger"></span>Theo dõi');
//}


function save(id){
    var name = "#idRe" +id;
    var idRe = $(name).val();
    var formData = new FormData();
    formData.append('idRe', idRe);
    $.ajax(
        {
            type: 'POST',
            url: '/applicant/save-job',
            contentType: false,
            processData: false,
            data: formData,
            success: function (result) {
                if(result == "false"){
                    showSwal('Bạn cần phải đăng nhập!', 'error');
                }else if(result == "true"){
                    showSwal('Lưu thành công!', 'success');
                }else{
                    showSwal('Bỏ lưu thành công', 'success');
                }
            },
            error: function (err) {
                alert(err);
            }
        }
    )
}

function save1(){
    var name = "#idReMain";
    var idRe = $(name).val();
    var formData = new FormData();
    formData.append('idRe', idRe);
    $.ajax(
        {
            type: 'POST',
            url: '/applicant/save-job',
            contentType: false,
            processData: false,
            data: formData,
            success: function (result) {
                if(result == "false"){
                    showSwal('Bạn cần phải đăng nhập!', 'error');
                }else if(result == "true"){
                    showSwal('Lưu thành công!', 'success');
                    $('#saveJobButton').html('<span class="icon-heart mr-2 text-danger"></span>Đã lưu');
                }else{
                    showSwal('Bỏ lưu thành công', 'success');
                    $('#saveJobButton').html('<span class="icon-heart-o mr-2 text-danger"></span>Lưu');
                }
            },
            error: function (err) {
            console.log(idRe);
                alert(err);
            }
        }
    )
}

//// Lưu trạng thái của nút cho mỗi id vào localStorage
//function saveButtonState(idUser, idRe, state) {
//    localStorage.setItem('buttonState' + idUser + idRe, state);
//}
//
//// Hàm áp dụng trạng thái nút từ localStorage khi trang được tải lại
//function applyButtonState(id) {
//    var buttonState = localStorage.getItem('buttonState' + id);
//    var applyButton = "#applyButton" + id;
//    var applyText = "#applyText" + id;
//
//    if (buttonState === 'hidden') {
//        $(applyButton).hide();
//        $(applyText).html("Đã ứng tuyển").css("color", "green").css("font-weight", "bold");
//    }
//}
var isSubmitting = false;
function apply(id){
    if (isSubmitting) {
         showSwal('Đang xử lý, vui lòng đợi!', 'error');
         return;
    }
    var name = "#idRe" +id;
    var nameModal = "#exampleModal" +id;
    var nameFile = "#fileUpload"+id;
    var nameText = "#text" +id;
    var applyButton = "#applyButton"+id;
    var applyText = "#applyText"+id;
    var idRe = $(name).val();
    var textValue = $(nameText).val();
    var fileUpload = $(nameFile).get(0);
    var files = fileUpload.files;
    var formData = new FormData();
    formData.append('file', files[0]);
    formData.append('idRe', idRe);
    formData.append('text', textValue);

    if(files[0] == null){
        showSwal('Bạn cần phải chọn CV!', 'error');
    } else {
        isSubmitting = true;
        $.ajax(
            {
                type: 'POST',
                url: '/applicant/apply-job',
                contentType: false,
                processData: false,
                data: formData,
                success: function (data) {
                    if (data == "false") {
                        showSwal('Bạn cần phải đăng nhập!', 'error');
                        isSubmitting = false;
                    } else if (data == "true") {

                        showSwal('Ứng tuyển thành công!', 'success');
                        $(nameModal).modal('hide');
                        $('#fileUpload').val("");
                        $(applyButton).hide();
                        $(applyText).html("Đã ứng tuyển").css("color", "green").css("font-weight", "bold");
                        isSubmitting = false;
                        reloadPage();
                    }
                },
                error: function (err) {
                    isSubmitting = false;
                    alert(err);
                }
            }
        )
    }
}

function apply1(id){
    if (isSubmitting) {
         showSwal('Đang xử lý, vui lòng đợi!', 'error');
         return;
    }
    var name = "#idRe" +id;
    var nameModal = "#exampleModal" +id;
    var nameFile = "#fileUpload"+id;
    var nameText = "#text" +id;
    var applyButton = "#applyButton"+id;
    var applyText = "#applyText"+id;
    var idRe = $(name).val();
    var textValue = $(nameText).val();
    var formData = new FormData();
    formData.append('idRe', idRe);
    formData.append('text', textValue);
    isSubmitting = true;
    $.ajax(
        {
            type: 'POST',
            url: '/applicant/apply-job1',
            contentType: false,
            processData: false,
            data: formData,
            success: function (data) {
                if(data == "false"){
                    showSwal('Bạn cần phải đăng nhập!', 'error');
                    isSubmitting = false;
                }else if(data == "true"){
                    showSwal('Ứng tuyển thành công!', 'success');
                    $(nameModal).modal('hide');
                    $('#fileUpload').val("");
                    $(applyButton).hide();
                    $(applyText).html("Đã ứng tuyển").css("color", "green").css("font-weight", "bold");
                    isSubmitting = false;
                    reloadPage();
                }else{
                    showSwal('Bạn chưa cập nhật CV!', 'error');
                    $(nameModal).modal('hide');
                    $('#fileUpload').val("");
                    isSubmitting = false;
                }
            },
            error: function (err) {
                alert(err);
            }
        }
    )
}

function choosed(id){
    var name = '#choose' + id;
    var name1 = '#loai1' + id;
    var name2 = '#loai2' + id;
    var button1 = '#button1' + id;
    var button2 = '#button2' + id;
    var value = $(name).val();
    if(value == 1){
        $(name1).css('display', 'block');
        $(name2).css('display', 'none');
        $(button1).css('display', 'block');
        $(button2).css('display', 'none');
    }else if(value==2){
        $(name2).css('display', 'block');
        $(name1).css('display', 'none');
        $(button2).css('display', 'block');
        $(button1).css('display', 'none');
    }else{
        $(name2).css('display', 'none');
        $(name1).css('display', 'none');
        $(button2).css('display', 'none');
        $(button1).css('display', 'none');
    }
}

function showModal(id) {
    var name = "#idRe" +id;
    var applyButton = "#applyButton"+id;
    var applyText = "#applyText"+id;
    var idRe = $(name).val();
    var formData = new FormData();
    formData.append('idRe', idRe);

    $.ajax({
        type: 'POST',
        url: '/user/show-modal-apply',
        processData: false,
        contentType: false,
        data: formData,
        success: function(data) {
            if(data === 'false') {
                showSwal('Bạn cần phải đăng nhập','error');
            }else if(data === 'duplicate'){
                showSwal('Bạn đã ứng tuyển rồi','error');
                $(applyButton).hide();
                $(applyText).html("Đã ứng tuyển").css("color", "green").css("font-weight", "bold");
            }else if(data === 'true') {
                $('#exampleModal'+id).modal('show');
            }
        },
        error: function(err) {
            alert(err);
        }
    });
}

function postJobSwal() {
    $.ajax({
        type: 'POST',
        url: '/recruiter/show-swal-post-job',
        processData: false,
        contentType: false,
        success: function(data) {
            if(data === 'false') {
                $('#exampleModalUpdate').modal('show');
            } else if(data === 'true') {
                window.location.href = '/recruiter/post-job';
            }
        },
        error: function(err) {
            alert(err);
        }
    });
}
