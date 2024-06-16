import { useContext, useEffect, useRef, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { endpoints } from "../../configs/APIs";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import Verify from "./Verify";

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
  const [classes, setClasses] = useState({});
  const [faculties, setFaculties] = useState({});
  const avatar = useRef();
  const nav = useNavigate();
  const u = useContext(MyUserContext);
  const Change = (event, field) => {
    setUser((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const Load = async () => {
    let f = await APIs.get(endpoints["faculty-list"]);
    let c = await APIs.get(endpoints["class-list"]);
    setFaculties(f.data);
    setClasses(c.data);
    console.log(1);
  };
  useEffect(() => {
    Load();
  }, []);

  useEffect(() => {
    if (u) {
      nav("/");
    }
  }, [u, nav]);
  const register = async (e) => {
    e.preventDefault();

    let form = new FormData();
    for (let key in user) {
      if (key !== "confirm") {
        form.append(key, user[key]);
      }
    }
    if (avatar) form.append("files", avatar.current.files[0]);

    try {
      let res = await APIs.post(endpoints["register"], form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      if (res.status === 201) nav("/login");
    } catch (ex) {
      console.error(ex);
    }
  };

  const RemainingField = () => (
    <Form onSubmit={register}>
      {fields.map((f) => (
        <Form.Group key={f.field} className="mb-3" controlId={f.field}>
          <Form.Label>{f.label}</Form.Label>
          <Form.Control
            onChange={(e) => Change(e, f.field)}
            value={user[f.field]}
            type={f.type}
            placeholder={f.label}
          />
        </Form.Group>
      ))}
      {classes && classes.length > 0 && (
        <>
          <Form.Group className="mb-3">
            <Form.Label>Lớp</Form.Label>
            <Form.Select defaultValue="" required>
              <option value="" disabled hidden>
                Lớp
              </option>
              {classes.map((c) => (
                <option value={c.id}>{c.name}</option>
              ))}
            </Form.Select>
          </Form.Group>
        </>
      )}
      {faculties && faculties.length > 0 && (
        <>
          <Form.Group className="mb-3">
            <Form.Label>Khoa</Form.Label>
            <Form.Select defaultValue="" required>
              <option value="" disabled hidden>
                Khoa
              </option>
              {faculties.map((f) => (
                <option value={f.id}>{f.name}</option>
              ))}
            </Form.Select>
          </Form.Group>
        </>
      )}

      <Form.Group className="mb-3" controlId="avatar">
        <Form.Control type="file" accept=".jpg,.png" ref={avatar} />
      </Form.Group>
      <Form.Group className="mb-3">
        <Button type="submit" value="primary">
          Đăng Ký
        </Button>
      </Form.Group>
    </Form>
  );

  const RenderStep = () => {
    console.log(user);
    switch (step) {
      case 1:
        return <Verify onSubmit={setUser} setStep={setStep} label="Email" placeholder="Email" endpoint="https://3e93-171-243-49-117.ngrok-free.app/TrainingPointApp/api/otp/create/" type="email"/>;
      case 2:
        return <Verify setStep={setStep} label={`Nhập mã OTP đã được gửi về email ${user["email"]}`} placeholder="Nhập OTP..." endpoint="https://3e93-171-243-49-117.ngrok-free.app/TrainingPointApp/api/otp/verify/" type="text" user={user}/>;
      case 3:
        return <RemainingField />;
    }
  }


  return (
    <Container className="custom-container">
      <h1 className="text-center">ĐĂNG KÝ</h1>
      <RenderStep />
    </Container>
  );
};

export default Register;
