import { useContext, useEffect, useReducer, useRef, useState } from "react";
import { authApi, endpoints } from "../../configs/APIs";
import { Button, Container, Form, Image } from "react-bootstrap";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { Link, useNavigate } from "react-router-dom";
import MyUserReducer from "../Reducer/UserReducer";
import cookie from "react-cookies";
import toast from "react-hot-toast";

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
    for (let key in user) {
      if (key != "confirm") {
        form.append(key, user[key]);
      }
    }
    if (avatar) form.append("files", avatar.current.files[0]);
    try {
      let res = await toast.promise(authApi().patch(endpoints["update-current-user"], form, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }), {loading: "Đang cập nhật", success: "Cập nhật thành công", error: (ex) =>{
        if(ex.response.status === 400){
          return "Nhập sai";
        } else return "Lỗi hệ thống"
      }});
      if (res.status === 200) {
        cookie.save("user", {
          ...u,
          userInfo: res.data,
        });
        dispatch({
          type: "update_user",
          payload: {
            resData: res.data,
            regData: u.userRegistration,
          },
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  useEffect(() => {
    if (u) {
      setFields([
        {
          label: "Tên Đăng Nhập",
          field: "username",
          value: u.userInfo.username,
          type: "text",
        },
        {
          label: "Họ",
          field: "firstName",
          value: u.userInfo.firstName,
          type: "text",
        },
        {
          label: "Tên",
          field: "lastName",
          value: u.userInfo.lastName,
          type: "text",
        },
        {
          label: "Email",
          field: "email",
          value: u.userInfo.email,
          type: "email",
        },
        {
          label: "Số Điện Thoại",
          field: "phone",
          value: u.userInfo.phoneNumber,
          type: "tel",
        },
        {
          label: "Mã Số Sinh Viên",
          field: "studentCode",
          value: u.userInfo.studentCode,
          type: "text",
        },
        {
          label: "Lớp",
          field: "className",
          value: u.userInfo.className,
          type: "text",
        },
        {
          label: "Khoa",
          field: "facultyName",
          value: u.userInfo.facultyName,
          type: "text",
        },
      ]);
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
              disabled={[
                "username",
                "email",
                "className",
                "studentCode",
                "facultyName",
              ].includes(f.field)}
            />
          </Form.Group>
        ))}
        <Form.Group className="mb-3" controlId="avatar">
          {/* <Form.Label>Avatar</Form.Label> */}
          <Image
            src={u?.userInfo.avatar}
            width="100"
            className="mb-3"
            rounded
          />
          <Form.Control type="file" accept=".jpg,.png,.jpeg" ref={avatar} />
        </Form.Group>
        <Link to="/change_password">
          <Button variant="primary" className="mb-3">
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
