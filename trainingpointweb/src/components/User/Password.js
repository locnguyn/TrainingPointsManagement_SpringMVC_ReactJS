import { Button, Container, Form, Alert } from "react-bootstrap";
import "./Styles.css";
import { useContext, useEffect, useState } from "react";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { authApi, endpoints } from "../../configs/APIs";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { updatePasswordFirebase } from "../../configs/firebase";

const Password = () => {
  const {user} = useContext(MyUserContext);
  const [userInfo, setUserInfo] = useState({});
  const [error, setError] = useState("");
  const {dispatch} = useContext(MyDispatcherContext);
  const [logout, setLogout] = useState(false);
  const [fields, setFields] = useState([]);
  const nav = useNavigate();

  const Change = (e, field) => {
    setUserInfo((current) => {
      const updatedUser = { ...current, [field]: e.target.value };

      if (
        updatedUser.old_password &&
        updatedUser.new_password &&
        updatedUser.confirm
      ) {
        if (updatedUser.new_password !== updatedUser.confirm) {
          setError("Không Khớp!");
        } else {
          setError("");
        }
      }
      return updatedUser;
    });
  };

  const Update = async (e) => {
    e.preventDefault();
    if (error || !userInfo.old_password || !userInfo.new_password || !userInfo.confirm)
      return;

    try {
      let res = await authApi().patch(endpoints["change-password"], userInfo, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      if (res.status === 200) {
        await updatePasswordFirebase(user.new_password);
        toast.success("Cập nhật thành công");
        dispatch({
          type: "logout",
        });
        setLogout(true);
      }
    } catch (ex) {
      if (ex.response.status === 400) toast.error("Sai Mật Khẩu");
      else toast.error(ex);
    }
  };

  useEffect(() => {
    if (user) {
      setFields([
        {
          label: "Mật Khẩu Cũ",
          field: "old_password",
          type: "password",
        },
        {
          label: "Mật Khẩu Mới",
          field: "new_password",
          type: "password",
        },
        {
          label: "Nhập Lại Mật Khẩu Mới",
          field: "confirm",
          type: "password",
        },
      ]);
    }
    if (logout) {
      nav("/login");
      setLogout(false);
    }
  }, [logout, nav]);

  return (
    <Container className="custom-container">
      <h1 className="text-center mb-3">Thay Đổi Mật Khẩu</h1>
      <Form onSubmit={Update}>
        {fields.map((f) => (
          <Form.Group key={f.field} controlId={f.field} className="mb-3">
            <Form.Label>{f.label}</Form.Label>
            <Form.Control
              onChange={(e) => Change(e, f.field)}
              type={f.type}
              value={userInfo[f.field]}
              required
            ></Form.Control>
          </Form.Group>
        ))}
        {error && (
          <Alert variant="danger" className="text-center">
            {error}
          </Alert>
        )}
        <Form.Group className="text-center m-1">
          <Button variant="success" type="submit" disabled={!!error}>
            Cập Nhật
          </Button>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default Password;
