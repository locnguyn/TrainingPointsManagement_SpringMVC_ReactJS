import { useContext, useEffect, useRef, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { endpoints } from "../../configs/APIs";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";

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
  ];

  const [user, setUser] = useState({});
  const avatar = useRef();
  const nav = useNavigate();
  const u = useContext(MyUserContext);
  const Change = (event, field) => {
    setUser((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

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

  return (
    <Container className="custom-container">
      <h1 className="text-center">ĐĂNG KÝ</h1>
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
        <Form.Group className="mb-3" controlId="avatar">
          <Form.Control type="file" accept=".jpg,.png" ref={avatar} />
        </Form.Group>
        <Form.Group className="mb-3">
          <Button type="submit" value="primary">
            Đăng Ký
          </Button>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default Register;
