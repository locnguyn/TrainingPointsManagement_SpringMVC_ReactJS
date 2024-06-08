import { useContext, useEffect, useState } from "react";
import { authApi, endpoints } from "../../configs/APIs";
import { Button, Container, Form, Image } from "react-bootstrap";
import { MyUserContext } from "../../configs/Contexts";
import { useNavigate } from "react-router-dom";

const User = () => {
  const [user, setUser] = useState({});
  const [userUpdate, setUserUpdate] = useState({});
  const u = useContext(MyUserContext);
  const nav = useNavigate();
  const currentUser = async () => {
    try {
      let res = await authApi().get(endpoints["current-user"]);
      setUser(res.data);
    } catch (ex) {
      console.error(ex);
    }
  };

  useEffect(() => {
    if (u) {
      currentUser();
    } else {
      nav("/");
    }
  }, [u, nav]);
  const fields = [
    {
      label: "Tên Đăng Nhập",
      field: "username",
      value: user.username,
      type: "text",
    },
    {
      label: "Họ",
      field: "firstName",
      value: user.firstName,
      type: "text",
    },
    {
      label: "Tên",
      field: "lastName",
      value: user.lastName,
      type: "text",
    },
    {
      label: "Email",
      field: "email",
      value: user.email,
      type: "email",
    },
    {
      label: "số Điện Thoại",
      field: "phone",
      value: user.phoneNumber,
      type: "tel",
    },
  ];

  const Change = (event, field) => {
    setUserUpdate((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const handleChangeAvatar = () => {
    // Implement avatar change logic
    console.log("Changing avatar...");
  };

  const handleChangePassword = () => {
    // Implement password change logic
    console.log("Changing password...");
  };

  const updateInfo = async (e) => {
    e.preventDefault();
    try {
      console.log({ ...user });
      console.log({ ...userUpdate });
      let res = await authApi().patch(
        endpoints["update-current-user"],
        { ...userUpdate },
        {
          headers: {
            "Content-Type": "multipart/form-data"
            
          },
        }
      );
      nav("/current-user");
    } catch (ex) {
      console.error(ex);
      // alert(ex);
    }
  };

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
              disabled={f.value === user.username}
            />
          </Form.Group>
        ))}
        {/* <Form.Group className="mb-3">
          <Form.Label>Tên Đăng Nhập</Form.Label>
          <Form.Control type="text" defaultValue={user?.username} disabled />
        </Form.Group> */}
        {/* <Form.Group>
          <Form.Label>Avatar</Form.Label>
          <Image src={user?.avatar} width="100" className="mb-3" />
          <Form.Control
            type="file"
            accept="image/*"
            onChange={handleChangeAvatar}
          />
        </Form.Group> */}
        <Button
          variant="primary"
          className="mb-3"
          onClick={handleChangePassword}
        >
          Đổi Mật Khẩu
        </Button>
        <Button variant="success" className="mb-3 ms-3" type="submit">
          Cập Nhật Thông Tin
        </Button>
      </Form>
    </Container>
  );
};

export default User;
