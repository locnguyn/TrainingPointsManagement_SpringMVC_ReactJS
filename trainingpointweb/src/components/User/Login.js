import { useContext, useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import cookie from "react-cookies";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { useLocation, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import "./Styles.css";
import { auth, database, setUserData } from "../../configs/firebase";
import { Timestamp, doc, setDoc } from "firebase/firestore";
import { createUserWithEmailAndPassword, signInWithEmailAndPassword } from "firebase/auth";
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

  const handleRegister = async (email, password, role) => {
    console.log(email, password, role);
    try {
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;
      // const docRef = await addDoc(collection(db, "users"), {
      //   email: user.email,
      //   role: role,
      // });
      // console.log("Document written with ID: ", docRef.id);

      // const a = await setDoc(doc(db, 'users', user.uid), {
      //   email: user.email,
      //   role: role,
      // });
      // console.log(a);
      setUserData(user.uid, {
        email: user.email,
        role: role,
      })
    } catch (error) {
      console.error(error);
      if (error.code === 'auth/email-already-in-use') {

      }
      throw error;
    }
  };

  const handleLoginFirebase = async (email, password) => {
    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;
      // console.log(user);
      // Cập nhật trạng thái người dùng
      setUserData(user.uid, {
        status: "online",
        lastActive: Timestamp.now()
      });
    }
    catch (e) {
      console.error(e);
      throw e;
    }

  }

  useEffect(() => {
    window.addEventListener('beforeunload', async () => {
      const user = auth.currentUser;
      if (user) {
        await setDoc(doc(db, "users", user.uid), {
          status: "offline",
          lastActive: Timestamp.now()
        }, { merge: true });
      }
    });
  }, []);

  const login = async (e) => {
    e.preventDefault();

    try {
      let res = await APIs.post(endpoints["login"], { ...user });
      cookie.save("token", res.data);
      let u = await authApi().get(endpoints["current-user"]);
      cookie.save("user", u.data);
      dispatch({
        type: "login",
        payload: u.data,
      });
      try {
        await handleRegister(u.data.email, user.password, u.data.role);
      } catch (e) {
        console.log(e);
        // try {
        // await handleLoginFirebase(u.data.email, user.password);
        // } catch (e) { }
        // if (e.code === "auth/invalid-credential") {
        //   try {
        //   } catch (e) {
        //     console.error(e);
        //   }
        // }
      }
      nav(location.state?.from?.pathname || "/");
    } catch (ex) {
      console.error(ex);
      if (ex.response.status === 400) {
        alert("Sai tài khoản/mật khẩu");
      } else {
        alert("Lỗi hệ thống");
      }
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
