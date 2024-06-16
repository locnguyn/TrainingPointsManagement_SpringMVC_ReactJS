import axios from "axios";
import { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import toast from "react-hot-toast";

const Verify = ({ onSubmit = () => { }, setStep = () => { }, label, placeholder, endpoint, type, user }) => {
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
            const res = await toast.promise(axios.post(endpoint, {
              email: email
            }), {
              loading: `Đang gửi mã OTP đến email ${email}`,
              success: `Đã gửi mã OTP đến email ${email}`,
              // error: "Xảy ra lỗi!"
            })

            if (res.status === 201) {
              onSubmit({ "email": email, "studentCode": email.substring(0, 10) });
              setStep(2);
            }
          }

        } catch (error) {
          if(error.response.status === 400){
            toast.error("Email đã được đăng ký!");
          }
          console.log(error);
        }
      }
    } else {
      try {
        console.log(endpoint, {
          email: user.email,
          otp: email
        })
        const res = await toast.promise(axios.get(endpoint, {
          headers: {
            "ngrok-skip-browser-warning": ""
          },
          params: {
            email: user.email,
            otp: email
          }
        }), {
          loading: "Đang xác thực mã OTP",
          // success: "Đã xác thực mã OTP",
          // error: "Xảy ra lỗi!"
        })
        console.log(res.data.message)
        if (res.data == "Verified") {
          console.log("verified");
          toast.success("Verified");
          setStep(3);
        } else if (res.data.message == "Mã OTP đã hết hạn, vui lòng nhập lại email!") {
          setStep(1);
          toast.error("Otp expired");
        } else if (res.data.message == "Otp not match"){
          toast.error("Otp not match");
        }
      } catch (error) {
        console.log(error);
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
