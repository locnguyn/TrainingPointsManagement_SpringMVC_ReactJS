import { useContext, useEffect, useRef, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { endpoints } from "../../configs/APIs";
import { useNavigate } from "react-router-dom";
import { MyUserContext, RegisterContext } from "../../configs/Contexts";
import Verify from "./Verify";
import RemainingField from "./RemainingField";

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

  const [user, setUser] = useState({});
  const [step, setStep] = useState(1);
  const [classes, setClasses] = useState([]);
  const [faculties, setFaculties] = useState([]);
  const avatar = useRef();
  const nav = useNavigate();
  const u = useContext(MyUserContext);

  const Change = (event, field) => {
    const value = event.target.value;
    setUser((current) => ({
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
      console.log('loading');
    Load();
  }, []);

  useEffect(() => {
    if (u) {
      nav("/");
    }
  }, [u, nav]);

  const register = async (e) => {
    e.preventDefault();

    const form = new FormData();
    for (const key in user) {
      if (key !== "confirm") {
        form.append(key, user[key]);
      }
    }
    if (avatar.current && avatar.current.files[0]) {
      form.append("files", avatar.current.files[0]);
    }

    try {
      const res = await APIs.post(endpoints["register"], form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      if (res.status === 201) nav("/login");
    } catch (ex) {
      console.error("Registration failed", ex);
    }
  };


  const RenderStep = () => {
    switch (step) {
      case 1:
        return <Verify onSubmit={setUser} setStep={setStep} label="Email" placeholder="Email" type="email" />;
      case 2:
        return <Verify setStep={setStep} label={`Nhập mã OTP đã được gửi về email ${user["email"]}`} placeholder="Nhập OTP..." type="text" user={user} />;
      case 3:
        return (
          <RegisterContext.Provider value={{Change, faculties, classes, avatar, register, fields, user}}>
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
