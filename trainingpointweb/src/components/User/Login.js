import { useContext, useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import cookie from "react-cookies";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { useLocation, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import "./Styles.css";
import { auth, handleLoginFirebase, handleRegisterFirebase, setUserData } from "../../configs/firebase";
import { Timestamp } from "firebase/firestore";
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
  const [user, setUser] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const dispatch = useContext(MyDispatcherContext);
  const u = useContext(MyUserContext);
  const Change = (event, field) => {
    setUser((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };

  useEffect(() => {
    if (u) {
      nav("/");
    }
  }, [u, nav]);

  const login = async (e) => {
    e.preventDefault();

    try {
      let res = await toast.promise(APIs.post(endpoints["login"], { ...user }), {
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
      let r = await authApi().get(endpoints["user-registration"]);
      cookie.save("user", {
        "userInfo": u.data,
        "userRegistration": r.data
      });
      dispatch({
        type: "login",
        payload: {
          resData: u.data,
          regData: r.data
        }
      });
      handleLoginFirebase(u.data.email, user.password);
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
                value={user[f.field]}
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
