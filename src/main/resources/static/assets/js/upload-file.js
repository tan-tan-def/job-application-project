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
//$(document).ready(function() {
    //Display the user's profile picture
//    $('#fileUpload').change(function(){
//        showImageAvatar(this, "#avatar");
//    });
    //Display the CV's applicant
//    $('#fileUpload1').change(function(){
//        showCVThumbnail(this);
//    });
    //Display the logo company
//    $('#fileUpload2').change(function(){
//        showImageAvatar(this, '#avatar1');
//    });
//})

//function showImageAvatar(fileInput, thumbnail){
//    var file = fileInput.files[0];
//    var reader = new FileReader();
//
//    reader.onload = function(e){
//        $(thumbnail).attr('src', e.target.result);
//    };
//    reader.readAsDataURL(file);
//}

//function showCVThumbnail(CVInput) {
//    var file = CVInput.files[0];
//    var objectURL = URL.createObjectURL(file);
//
//    $('#CVThumbnail').attr('href', objectURL);
//    $('#CVThumbnail').css('display', 'block');
//}

//Upload avatar
$(function () {
    $('#fileUpload').change(function () {
        if (window.FormData !== undefined) {
            var fileUpload = $('#fileUpload').get(0);
            var files = fileUpload.files;
            var email = $("#job-email").val();
            var formData = new FormData();
            formData.append('file', files[0]);
            formData.append('email', email);
            if(files[0] == null){
                // document.getElementById("change").style.backgroundColor = 'red';
                // $('#text').val(" ❌ Cập nhật ảnh thất bại");
                $(".toast").text("Vui lòng chọn ảnh đại diện").toast("show");
            } else {
                $.ajax(
                    {
                        type: 'POST',
                        url: '/user/upload-avatar',
                        contentType: false,
                        processData: false,
                        data: formData,
                        success: function (urlImage) {
                            console.log(urlImage)
                            if(urlImage == "Error"){
                                document.getElementById("change").style.backgroundColor = 'red';
                                showSwal('Cập nhật ảnh đại diện thất bại','error')
                                $("#divImage").css("display","block")
                            }else{
                                $('#avatar').attr('src', urlImage)
                                showSwal('Cập nhật ảnh đại diện thành công','success')
                                console.log(urlImage)
                            }
                        },
                        error: function (err) {
                            alert(err);
                        }
                    }
                )
            }

        }
    })
})

//Upload logo for company
$(function () {
    $('#fileUpload2').change(function () {
        if (window.FormData !== undefined) {
            var fileUpload = $('#fileUpload2').get(0);
            var files = fileUpload.files;
//            var email = $("#job-email").val();
            var formData = new FormData();
            formData.append('file', files[0]);
//            formData.append('email', email);
            if(files[0] == null){
                $(".toast").text("Vui lòng chọn logo").toast("show");
            } else {
                $.ajax(
                    {
                        type: 'POST',
                        url: '/recruiter/upload-logo-company',
                        contentType: false,
                        processData: false,
                        data: formData,
                        success: function (urlImage) {
                            if(urlImage == "Error"){
                                document.getElementById("change").style.backgroundColor = 'red';
                                showSwal('Cập nhật logo thất bại!', 'error');
                                $("#divLogo").css("display","block");
                            }else{
                                $('#avatar1, #avatar1-same').attr('src', urlImage);
                                showSwal('Cập nhật logo thành công!', 'success')
                            }
                        },
                        error: function (err) {
                            alert(err);
                        }
                    }
                )
            }
        }
    })
})
//Upload CV
$(function () {
    $('#fileUpload1').change(function () {
        if (window.FormData !== undefined) {
            var fileUpload = $('#fileUpload1').get(0);
            var files = fileUpload.files;
            var formData = new FormData();
            formData.append('file', files[0]);
            if(files[0] == null){
                // document.getElementById("change").style.backgroundColor = 'red';
                // $('#text').val(" ❌ Cập nhật ảnh thất bại");
                $(".toast").toast("show");
            } else {
                $.ajax(
                    {
                        type: 'POST',
                        url: '/applicant/upload-cv',
                        contentType: false,
                        processData: false,
                        data: formData,
                        success: function (urlImage) {
                            console.log(urlImage)
                            if(urlImage == "false"){
                                showSwal("Cập nhật CV thất bại!","error")
                            }else{
                                showSwal("Cập nhật CV thành công!", "success")
                                reloadPage();
                            }

                        },
                        error: function (err) {
                            alert(err);
                        }
                    }
                )
            }

        }
    })
})




