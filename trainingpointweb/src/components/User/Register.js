import { useContext, useEffect, useRef, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { endpoints } from "../../configs/APIs";
import { useNavigate } from "react-router-dom";
import { MyUserContext, RegisterContext } from "../../configs/Contexts";
import Verify from "./Verify";
import RemainingField from "./RemainingField";
import toast from "react-hot-toast";
import { handleRegisterFirebase } from "../../configs/firebase";

const Register = () => {
  const fields = [
    {
      field: "username",
      type: "text",
      label: "Tên đăng nhập",
    },
    {
      field: "password",
      type: "password",
      label: "Mật khẩu",
    },
    {
      field: "lastName",
      type: "text",
      label: "Họ",
    },
    {
      field: "firstName",
      type: "text",
      label: "Tên",
    },

    {
      field: "email",
      type: "email",
      label: "Email",
    },
    {
      field: "phone",
      type: "tel",
      label: "Số điện thoại",
    },
    {
      field: "confirm",
      type: "password",
      label: "Xác nhận mật khẩu",
    },
    {
      field: "studentCode",
      type: "text",
      label: "Mã số sinh viên",
    },
  ];

  const [userInfo, setUserInfo] = useState({});
  const [step, setStep] = useState(1);
  const [classes, setClasses] = useState([]);
  const [faculties, setFaculties] = useState([]);
  const avatar = useRef();
  const nav = useNavigate();
  const {user} = useContext(MyUserContext);

  const Change = (event, field) => {
    const value = event.target.value;
    setUserInfo((current) => ({
      ...current,
      [field]: value,
    }));
  };

  const Load = async () => {
    try {
      const [f, c] = await Promise.all([
        APIs.get(endpoints["faculty-list"]),
        APIs.get(endpoints["class-list"]),
      ]);
      setFaculties(f.data);
      setClasses(c.data);
    } catch (error) {
      console.error("Failed to load faculties or classes", error);
    }
  };

  useEffect(() => {
    Load();
  }, []);

  useEffect(() => {
    if (user) {
      nav("/");
    }
  }, [user, nav]);

  const register = async (e) => {
    e.preventDefault();

    const form = new FormData();
    for (const key in userInfo) {
      if (key !== "confirm") {
        form.append(key, userInfo[key]);
      }
    }
    if (avatar.current && avatar.current.files[0]) {
      form.append("files", avatar.current.files[0]);
    }

    try {
      const res = await toast.promise(APIs.post(endpoints["register"], form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }), {
        success: "Đăng ký thành công",
        loading: "Đang xử lý...",
        error: "Tên đăng nhập đã tồn tại!"
      });
      if (res.status === 201) {
        handleRegisterFirebase(userInfo["email"], userInfo["password"], "ROLE_USER", userInfo["firstName"], userInfo["lastName"], res.data);
        nav("/login")
      };
    } catch (ex) {
      console.error("Registration failed", ex);
    }
  };


  const RenderStep = () => {
    switch (step) {
      case 1:
        return <Verify onSubmit={setUserInfo} setStep={setStep} label="Email" placeholder="Email" type="email" />;
      case 2:
        return <Verify setStep={setStep} label={`Nhập mã OTP đã được gửi về email ${userInfo["email"]}`} placeholder="Nhập OTP..." type="text" user={userInfo} />;
      case 3:
        return (
          <RegisterContext.Provider value={{Change, faculties, classes, avatar, register, fields, user: userInfo}}>
            <RemainingField />
          </RegisterContext.Provider>
        );
      default:
        return null;
    }
  };

  return (
    <Container className="custom-container">
      <h1 className="text-center">ĐĂNG KÝ</h1>
      {RenderStep()}
    </Container>
  );
};

export default Register;
