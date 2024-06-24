import { useContext, useEffect, useReducer, useRef, useState } from "react";
import { authApi, endpoints } from "../../configs/APIs";
import { Button, Card, Container, Figure, Form, Image } from "react-bootstrap";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { Link, useNavigate } from "react-router-dom";
import MyUserReducer from "../Reducer/UserReducer";
import cookie from "react-cookies";
import toast from "react-hot-toast";

const User = () => {
  const [userInfo, setUserInfo] = useState({});
  const {user} = useContext(MyUserContext);
  const {dispatch} = useContext(MyDispatcherContext);
  const nav = useNavigate();
  const avatar = useRef();
  const [fields, setFields] = useState([]);

  const Change = (event, field) => {
    setUserInfo((current) => {
      return { ...current, [field]: event.target.value };
    });
  };

  const updateInfo = async (e) => {
    e.preventDefault();
    let form = new FormData();
    for (let key in userInfo) {
      if (key != "confirm") {
        form.append(key, userInfo[key]);
      }
    }
    if (avatar) form.append("files", avatar.current.files[0]);
    try {
      let res = await toast.promise(
        authApi().patch(endpoints["update-current-user"], form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }),
        {
          loading: "Đang cập nhật",
          success: "Cập nhật thành công",
          error: (ex) => {
            if (ex.response.status === 400) {
              return "Nhập sai";
            } else return "Lỗi hệ thống";
          },
        }
      );
      if (res.status === 200) {
        // cookie.save("user", {
        //   ...user,
        //   userInfo: res.data,
        // });
        dispatch({
          type: "update_user",
          payload: res.data
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  useEffect(() => {
    if (user) {
      setFields([
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
          label: "Số Điện Thoại",
          field: "phone",
          value: user.phoneNumber,
          type: "tel",
        },
        {
          label: "Email",
          field: "email",
          value: user.email,
          type: "email",
        },
        {
          label: "Mã Số Sinh Viên",
          field: "studentCode",
          value: user.studentCode,
          type: "text",
        },
        {
          label: "Lớp",
          field: "className",
          value: user.className,
          type: "text",
        },
        {
          label: "Khoa",
          field: "facultyName",
          value: user.facultyName,
          type: "text",
        },
      ]);
    } else {
      nav("/");
    }
  }, [user, nav]);
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
        <Form.Group
          className="d-flex flex-column align-items-center"
          controlId="avatar"
        >
          <Card style={{ width: "18rem" }} className="mb-4">
            <Card.Img variant="top" src={user?.avatar} />
            <Card.Body>
              <Card.Title>Ảnh đại diện</Card.Title>
              <Form.Control type="file" accept=".jpg,.png,.jpeg" ref={avatar} />
            </Card.Body>
          </Card>
          {/* <Form.Label>Avatar</Form.Label> */}
          {/* <Image
            src={u?.avatar}
            width="100"
            className="mb-3"
            rounded
          /> */}
          {/* <Figure>
            <Figure.Image src={u?.avatar}
            width="100"
            className="mb-3"
            rounded />
            <Figure.Caption>Ảnh đại diện</Figure.Caption>
          </Figure> */}
          <div>
          <Link to="/change-password">
            <Button variant="primary" className="mb-3">
              Đổi Mật Khẩu
            </Button>
          </Link>

          <Button variant="success" className="mb-3 ms-3" type="submit">
            Cập Nhật
          </Button>
        </div>
        </Form.Group>

      </Form>
    </Container>
  );
};

export default User;
