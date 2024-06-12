import { Button, Container, Image, Nav, Navbar } from "react-bootstrap";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import "./Styles.css";

const Header = () => {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const nav = useNavigate();
  const location = useLocation();
  // const u = useContext(MyUserContext);
  const [logout, setLogout] = useState(false);

  const Logout = () => {
    try {
      dispatch({
        type: "logout",
      });
      setLogout(true);
    } catch (ex) {
      console.error(ex);
    }
  };
  useEffect(() => {
    if (logout) {
      nav("/");
      setLogout(false);
    }
  }, [logout, nav]);

  return (
    <>
      <Navbar collapseOnSelect expand="lg" className="custom-navbar">
        <Container>
          <Navbar.Brand>
            <Link to="/" className="nav-link">Điểm Rèn Luyện</Link>
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="me-auto">
              <Link className="nav-link" to="/">
                Trang Chủ
              </Link>
              <Link className="nav-link" to="/activity">
                Hoạt Động
              </Link>
              <Link className="nav-link" to="/report_missing">
                Báo Thiếu
              </Link>
            </Nav>
            <Nav className="ms-auto">
              {user === null ? (
                <>
                  <Link className="me-2" to="/register">
                    <Button variant="primary">Đăng Ký</Button>
                  </Link>
                  <Link to="/login" state={{ prev: location }}>
                    <Button variant="success">Đăng Nhập</Button>
                  </Link>
                </>
              ) : (
                <>
                  <Link to="/current-user">
                    <Image
                      className="me-2"
                      src={user.avatar}
                      style={{
                        width: "40px",
                        height: "40px",
                        objectFit: "cover",
                      }}
                      roundedCircle
                    />
                  </Link>
                  <Link onClick={Logout}>
                    <Button variant="success">Đăng Xuất</Button>
                  </Link>
                </>
              )}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

export default Header;
