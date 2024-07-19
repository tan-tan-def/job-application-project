var email = document.querySelector("#job-email");
var fullName = document.querySelector("#job-full-name");
var address = document.querySelector("#job-location");
var phoneNumber = document.querySelector("#job-phone-number");

var emailCompany = document.querySelector("#job-email-company");
var fullNameCompany = document.querySelector("#job-full-name-company");
var addressCompany = document.querySelector("#job-location-company");
var phoneNumberCompany = document.querySelector("#job-phone-number-company");

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

//check phone number
function checkPhoneNumber(input){
    const regexPhoneNumber = /((09|03|07|08|05)+([0-9]{8})\b)/g;
    input.value = input.value.trim();
    let isPhoneNumberError = false;
    if (regexPhoneNumber.test(input.value)) {
        showSuccess(input);
      } else {
        isPhoneNumberError = true;
        showError(input, "Số điện thoại không hợp lệ");
      }
      return isPhoneNumberError;
}

//Validate form User
function validateForm() {
  let isEmptyError = checkEmptyError([email,fullName,address,phoneNumber]);
  let isEmailError = checkEmailError(email);
  let isPhoneNumberError = checkPhoneNumber(phoneNumber);
  if ( isEmptyError || isEmailError || isPhoneNumberError) {
    return false;
    }
  return true;
}
function validateFormCompany() {
  let isEmptyError = checkEmptyError([emailCompany,fullNameCompany,addressCompany,phoneNumberCompany]);
  let isEmailError = checkEmailError(emailCompany);
  let isPhoneNumberError = checkPhoneNumber(phoneNumberCompany);
  if ( isEmptyError || isEmailError || isPhoneNumberError) {
    return false;
    }
  return true;
}
