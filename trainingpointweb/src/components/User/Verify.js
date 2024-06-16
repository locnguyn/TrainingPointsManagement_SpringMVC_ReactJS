import { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import toast from "react-hot-toast";
import APIs, { endpoints } from "../../configs/APIs";
import { IoClose } from "react-icons/io5";
import Toast, { ShowErrorToast } from "../Common/Toast";

const Verify = ({ onSubmit = () => { }, setStep = () => { }, label, placeholder, type, user }) => {
  const [email, setEmail] = useState("");
  const validateEmailFormat = (email) => {
    const regex = /^\d{10}[a-z]+@ou\.edu\.vn$/
    return regex.test(email);
  };
  const verifyEmail = async (e) => {
    e.preventDefault();
    if (type === "email") {
      if (!validateEmailFormat(email)) {
        toast.error("Đây không phải email trường cung cấp, vui lòng kiểm tra lại");
      } else {
        try {
          if (type === "email") {
            const res = await toast.promise(APIs.post(endpoints['create-otp'], {
              email: email
            }), {
              loading: `Đang gửi mã OTP đến email ${email}`,
              success: `Đã gửi mã OTP đến email ${email}`,
              error: (e)=>e.response.status === 400 ? "Email đã được đăng ký!" : "Có lỗi xảy ra, vui lòng thử lại sau"
            })

            if (res.status === 201) {
              onSubmit({ "email": email, "studentCode": email.substring(0, 10) });
              setEmail("");
              setStep(2);
            }
          }

        } catch (error) {
          if (error.response.status === 400) {
            console.log(error);
          }
        }
      }
    } else {
      try {
        console.log({
          email: user.email,
          otp: email
        })

        const res = await APIs.get(endpoints['verify-otp'], {
          params: {
            email: user.email,
            otp: email
          }
        })
        if (res.data.message == "Verified") {
          toast.success("Xác thực thành công!");
          setStep(3);
        } else if (res.data.message == "Otp expired") {
          setStep(1);
          toast.error("Mã OTP đã hết hạn, vui lòng nhập lại email!");
        } else if (res.data.message == "Otp not match") {
          toast.error("Mã OTP không khớp!");
        }
      } catch (error) {
        toast.error("Xảy ra lỗi!");
        // console.log(error);
        console.log(error.response.data);
      }
    }
  }

  return (
    <Form onSubmit={verifyEmail}>
      <Form.Group className="mb-3">
        <Form.Label>{label}</Form.Label>
        <Form.Control type={type} placeholder={placeholder} value={email} required onChange={(e) => setEmail(e.target.value)}></Form.Control>
      </Form.Group>
      <Form.Group className="mb-3">
        <Button type="submit" variant="primary">
          Tiếp Tục
        </Button>
      </Form.Group>
    </Form>
  );
};

export default Verify;
