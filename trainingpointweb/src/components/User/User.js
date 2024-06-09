import { useContext, useEffect, useReducer, useRef, useState } from "react";
import { authApi, endpoints } from "../../configs/APIs";
import { Button, Container, Form, Image } from "react-bootstrap";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { Link, useNavigate } from "react-router-dom";
import MyUserReducer from "../Reducer/UserReducer";
import cookie from "react-cookies";


const User = () => {
  const [user, setUser] = useState({});
  const u = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const nav = useNavigate();
  const avatar = useRef();
  const [fields, setFields] = useState([]);

  const Change = (event, field) => {
    setUser((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const updateInfo = async (e) => {
    e.preventDefault();
    let form = new FormData();
    for (let key in user){
      if (key != "confirm"){
        form.append(key,  user[key]);
      }
    }
    if (avatar) form.append("files", avatar.current.files[0]);
    try {
      let res = await authApi().patch(
        endpoints["update-current-user"],
        form,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      if (res.status === 200) {
        cookie.save("user", res.data);
        dispatch({
          type: "update_user",
          payload: res.data
        });
        alert("Thành công");
      }
    } catch (ex) {
      console.error(ex);
      if (ex.response.status === 400) {
        alert("Nhập Sai");
      } else {
        alert("Lỗi hệ thống");
      }
    }
  };

  useEffect(() => {
    if (u) {
      setFields([{
        label: "Tên Đăng Nhập",
        field: "username",
        value: u.username,
        type: "text",
      },
      {
        label: "Họ",
        field: "firstName",
        value: u.firstName,
        type: "text",
      },
      {
        label: "Tên",
        field: "lastName",
        value: u.lastName,
        type: "text",
      },
      {
        label: "Email",
        field: "email",
        value: u.email,
        type: "email",
      },
      {
        label: "Số Điện Thoại",
        field: "phone",
        value: u.phoneNumber,
        type: "tel",
      },])
    } else {
      nav("/");
    }
  }, [u, nav]);
  return (
    <Container className="custom-container">
      <h1 className="text-center">Thông Tin Người Dùng</h1>
      <Form onSubmit={updateInfo}>
        {fields.map((f) => (
          <Form.Group className="mb-3" key={f.field} controlId={f.field}>
            <Form.Label>{f.label}</Form.Label>
            <Form.Control
              onChange={(e) => Change(e, f.field)}
              type={f.type}
              defaultValue={f.value}
              disabled={f.field === "username"}
            />
          </Form.Group>
        ))}
        <Form.Group className="mb-3" controlId="avatar">
          {/* <Form.Label>Avatar</Form.Label> */}
          <Image src={u?.avatar} width="100" className="mb-3" rounded />
          <Form.Control
            type="file"
            accept = ".jpg,.png"
            ref={avatar}
          />
        </Form.Group>
        <Link to="/change_password">
        <Button
          variant="primary"
          className="mb-3"
        >
          Đổi Mật Khẩu
        </Button>
        </Link>
        
        <Button variant="success" className="mb-3 ms-3" type="submit">
          Cập Nhật
        </Button>
      </Form>
    </Container>
  );
};

export default User;
