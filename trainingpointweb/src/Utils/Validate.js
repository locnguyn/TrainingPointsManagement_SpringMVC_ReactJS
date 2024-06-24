const Validate = {
    validateEmail: (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },
    validatePassword: (password) => {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;
        return passwordRegex.test(password);
    },
    validatePhoneNumber: (phoneNumber) => {
        const phoneNumberRegex = /^(0[0-9]{9,10})$/;
        return phoneNumberRegex.test(phoneNumber);
    },
    validatePassword: (password) => {
        if (password.length < 8) {
            return false;
        }
        const hasNumber = /\d/.test(password);
        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password);

        if (!(hasNumber && hasUpperCase && hasLowerCase && hasSpecialChar)) {
            return false;
        }

        return true;
    },
}

export default Validate;