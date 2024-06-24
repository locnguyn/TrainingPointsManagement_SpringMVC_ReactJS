import { useContext, useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import cookie from "react-cookies";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { useLocation, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import "./Styles.css";
import { handleLoginFirebase } from "../../configs/firebase";
import toast from "react-hot-toast";

const Login = () => {
  const fields = [
    {
      label: "Tên đăng nhập",
      type: "text",
      field: "username",
    },
    {
      label: "Mật khẩu",
      type: "password",
      field: "password",
    },
  ];

  const nav = useNavigate();
  const location = useLocation();
  const [userInfo, setUserInfo] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const { dispatch} = useContext(MyDispatcherContext);
  const { user} = useContext(MyUserContext);
  const Change = (event, field) => {
    setUserInfo((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };

  useEffect(() => {
    if (user) {
      nav("/");
    }
  }, [user, nav]);

  const login = async (e) => {
    e.preventDefault();

    try {
      let res = await toast.promise(APIs.post(endpoints["login"], { ...userInfo }), {
        loading: "Đang xử lý...",
        success: "Đăng nhập thành công",
        error: (ex) => {
          if (ex.response.status === 400) {
            return "Sai tài khoản/mật khẩu";
          } else {
            return "Lỗi hệ thống";
          }
        }
      });
      cookie.save("token", res.data);
      let u = await authApi().get(endpoints["current-user"]);
      let a = await authApi().get(endpoints["user-registration"]);
      let report = await authApi().get(endpoints["user-report"]);
      console.log(a.data);
      handleLoginFirebase(u.data.email, userInfo.password);
      dispatch({
        type: "login",
        payload: u.data
      });
      nav(location.state?.from?.pathname || "/");
    } catch (ex) {
      console.error(ex);
    }
  };

  return (
    <Container className="custom-container">
      <h1 className="text-center mt-1">ĐĂNG NHẬP</h1>
      <Form onSubmit={login}>
        {fields.map((f) => (
          <Form.Group key={f.field} className="mb-3" controlId={f.field}>
            <Form.Label>{f.label}</Form.Label>
            <div className="d-flex align-items-center">
              <Form.Control
                onChange={(e) => Change(e, f.field)}
                value={userInfo[f.field] ? userInfo[f.field] : ''}
                type={
                  f.field === "password"
                    ? showPassword
                      ? "text"
                      : "password"
                    : f.type
                }
                placeholder={f.label}
              />
              {f.field === "password" && (
                <Button variant="light" onClick={togglePasswordVisibility}>
                  <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
                </Button>
              )}
            </div>
          </Form.Group>
        ))}
        <Form.Group className="mb-3">
          <Button type="submit" value="primary">
            Đăng nhập
          </Button>
        </Form.Group>
      </Form>
    </Container>
  );
};

export default Login;
