var fullName = document.querySelector("#full-name");
var email = document.querySelector("#email");
var password = document.querySelector("#password");
var confirmPassword = document.querySelector("#confirm-password");
var role = document.querySelector("#role-select");
var formControls = document.querySelectorAll(".form-control");
var form = document.querySelector("form");
var isDuplicateEmail = false;

//show error
function showError(input, message) {
  let parent = input.parentElement;
  let small = parent.querySelector("small");
  parent.classList.add("error");
  small.innerText = message;
}

//show success
function showSuccess(input) {
  let parent = input.parentElement;
  let small = parent.querySelector("small");
  parent.classList.remove("error");
  small.innerText = "";
}

//check empty
function checkEmptyError(listInput) {
  let isEmptyError = false;
  listInput.forEach((input) => {
    input.value = input.value.trim();
    if (!input.value) {
      isEmptyError = true;
      showError(input, "Không được để trống");
    } else {
      showSuccess(input);
    }
  });
  return isEmptyError;
}

//check email
function checkEmailError(input) {
  const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  input.value = input.value.trim();
  let isEmailError = false;
  if (regexEmail.test(input.value)) {
    showSuccess(input);
  } else {
    isEmailError = true;
    showError(input, "Email không hợp lệ");
  }
  return isEmailError;
}

//check length fullName
function checkLengthError(input, min) {
  let isFullNameError = false;
  input.value = input.value.trim();
  if (input.value.length < min) {
    isFullNameError = true;
    showError(input, "Vui lòng nhập đúng họ và tên");
  } else {
    showSuccess(input);
  }
  return isFullNameError;
}

//check password
function checkPasswordError(input) {
  input.value = input.value.trim();
  const regexPassword =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[\w@$!%*?&]{8,}$/;
  let isPasswordError = false;
  if (regexPassword.test(input.value)) {
    showSuccess(input);
  } else {
    isPasswordError = true;
    showError(
      input,
      "Chứa ít nhất 8 kí tự, phải bao gồm các kí tự in hoa, in thường, kí tự số, kí tự đặc biệt(@, $, !, %, *, ?, &) "
    );
  }
  return isPasswordError;
}

//check confirm password
function checkConfirmPasswordError(password, rePassword) {
  let isConfirmPasswordError = false;
  password.value = password.value.trim();
  rePassword.value = rePassword.value.trim();
  if (password.value === rePassword.value) {
    showSuccess(rePassword);
  } else {
    isConfirmPasswordError = true;
    showError(rePassword, "Mật khẩu chưa trùng khớp");
  }
  return isConfirmPasswordError;
}
//check duplicate email
function checkDuplicateEmail() {
    var enterEmail = email.value.trim();

        $.ajax({
            url: 'check-duplicate-email',
            data: {email: enterEmail},
            success: function (result) {
                if(result=='duplicate') {
                    showError(email, "Email đã được đăng kí, vui lòng dùng email khác");
                    isDuplicateEmail = true;
                }else{
                    isDuplicateEmail=false;
                }
            }
        });
    return isDuplicateEmail;
}

//blur
formControls.forEach((formControlFirstChild) => {
  var firstChild = formControlFirstChild.querySelector(":first-child");

  firstChild.addEventListener("blur", function () {
    switch (firstChild.id) {
      case "fullName":
        checkLengthError(fullName, 3);
        break;
      case "email":
        var isEmailDuplicate = checkDuplicateEmail();
        if(!isEmailDuplicate){
            checkEmailError(email);}
        break;
      case "password":
        checkPasswordError(password);
        break;
      case "confirm-password":
        checkConfirmPasswordError(password, confirmPassword);
        break;
      default:
        if (firstChild.value.trim() === "") {
          showError(firstChild, "Không được để trống");
        }
        break;
    }
  });
  firstChild.addEventListener("focus", function () {
    showSuccess(firstChild);
  });
});

function validateForm() {
  let isEmptyError = checkEmptyError([fullName, email,password, confirmPassword, role]);
  let isPasswordError = checkPasswordError(password);
  let isEmailError = checkEmailError(email);
  let isLengthFullNameError = checkLengthError(fullName, 3);
  let isConfirmPasswordError = checkConfirmPasswordError(password, confirmPassword);
  if ( isPasswordError        ||
       isEmailError           ||
       isLengthFullNameError  ||
       isConfirmPasswordError ||
       isDuplicateEmail) {
       if(isDuplicateEmail){
            showError(email, "Email đã được đăng kí, vui lòng dùng email khác");
       }
    return false;
    }
  return true;
}
