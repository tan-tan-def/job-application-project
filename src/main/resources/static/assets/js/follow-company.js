function showSwal(title, type) {
    swal({
        title: title,
        icon: type === 'success' ? 'success' : 'error',
        timer: 3000,
        buttons: true,
        type: type
    });
}
//function reloadPage() {
//    setTimeout(function(){
//        window.location.reload();
//    }, 500);
//}
//var buttonState = localStorage.getItem('buttonState');
//if (buttonState === 'followed') {
//    $('#followButton').html('<span class="icon-heart mr-2 text-danger"></span>Đã theo dõi');
//} else {
//    $('#followButton').html('<span class="icon-heart-o mr-2 text-danger"></span>Theo dõi');
//}

function follow(){
    var name = "#idCompany";
    var idCompany = $(name).val();
    var formData = new FormData();
    formData.append('idCompany', idCompany);
    $.ajax(
        {
            type: 'POST',
            url: '/applicant/follow-company',
            contentType: false,
            processData: false,
            data: formData,
            success: function (result) {
                if(result == "false"){
                    showSwal('Bạn cần phải đăng nhập!', 'error');
                }else if(result == "true"){
                    showSwal('Lưu thành công!', 'success');
                    //reloadPage()
                    $('#followButton').html('<span class="icon-heart mr-2 text-danger"></span>Đã theo dõi');
                }else{
                    showSwal('Bỏ lưu thành công', 'success');
                    //reloadPage()
                    $('#followButton').html('<span class="icon-heart-o mr-2 text-danger"></span>Theo dõi');
                }
            },
            error: function (err) {
                alert(err);
            }
        }
    )
}
//function deleteFollow(companyId) {
//    $.ajax({
//        type: 'GET',
//        url: '/applicant/delete-follow/' + followCompanyId,
//        contentType: false,
//        processData: false,
//        success: function(result) {
//            if(result==='deleteSuccess'){
//                $('#followButton').html('<span class="icon-heart-o mr-2 text-danger"></span>Theo dõi');
//                localStorage.setItem('buttonState', 'unfollowed');
//            }
//        },
//        error: function(err) {
//            alert(err);
//        }
//    });
//}