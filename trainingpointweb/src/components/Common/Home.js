import React, { useContext, useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Button,
  Table,
  Dropdown,
  DropdownButton,
  DropdownItem,
} from "react-bootstrap";
import { Link, useLocation, useNavigate, useSearchParams } from "react-router-dom";
import "./Styles.css";
import { MyUserContext } from "../../configs/Contexts";
import APIs, { authApi, endpoints } from "../../configs/APIs";

const Home = () => {
  const [activities, setActivities] = useState([]);
  const [faculty, setFaculty] = useState([]);
  const [article, setArticle] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const location = useLocation();
  const nav = useNavigate();
  const user = useContext(MyUserContext);

  const load = async (filters = {}) => {
    let params = new URLSearchParams(filters);
    let res = await APIs.get(`${endpoints["activity-list"]}?${params}`);
    let fac = await APIs.get(endpoints["faculty-list"]);
    let art = await APIs.get(endpoints["article-list"]);
    setActivities(res.data);
    setFaculty(fac.data);
    setArticle(art.data);

  };

  const updateFilter = (key, value) => {
    if (value === "all") {
      searchParams.delete(key);
    } else {
      searchParams.set(key, value);
    }
    setSearchParams(searchParams);
  };


  useEffect(() => {
    let filters = Object.fromEntries([...searchParams]);
    load(filters);
  }, [searchParams]);

  return (
    <Container className="home-container">
      <Row className="justify-content-center text-center">
        <Col md={8}>
          <h1 className="mt-5">
            Chào Mừng Đến Với Hệ Thống Quản Lý Điểm Rèn Luyện
          </h1>
          <p className="lead mt-3">
            Đây là hệ thống quản lý điểm rèn luyện cho sinh viên. Bạn có thể
            theo dõi và cập nhật các hoạt động và điểm rèn luyện của mình tại
            đây.
          </p>
          <div className="mt-4">
            <Link to="/activity">
              <Button variant="primary" className="me-2">
                Xem Hoạt Động
              </Button>
            </Link>
            <Link to="/report_missing">
              <Button variant="secondary">Báo Thiếu</Button>
            </Link>
          </div>
        </Col>
      </Row>
      <Dropdown className="d-inline mx-2">
        <Dropdown.Toggle variant="success">Khoa</Dropdown.Toggle>
        <Dropdown.Menu>
          <Dropdown.Item onClick={() => updateFilter('faculty', 'all')}>
            Tất cả
          </Dropdown.Item>
          {faculty.map((f) => (
            <Dropdown.Item key={f.id} onClick={() => updateFilter('faculty', f.id)}>
              {f.name}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      </Dropdown>
      <Dropdown className="d-inline mx-2">
        <Dropdown.Toggle variant="success">Điều</Dropdown.Toggle>
        <Dropdown.Menu>
          <Dropdown.Item onClick={() => updateFilter('article', 'all')}>
            Tất cả
          </Dropdown.Item>
          {article.map((a) => (
            <Dropdown.Item key={a.id} onClick={() => updateFilter('article', a.id)}>
              {a.name}
            </Dropdown.Item>
          ))}
        </Dropdown.Menu>
      </Dropdown>
      <Dropdown className="d-inline mx-2">
        <Dropdown.Toggle variant="success">Thời Gian</Dropdown.Toggle>
        <Dropdown.Menu>
          <Dropdown.Item onClick={() => updateFilter('filterType', 'all')}>
            Tất cả
          </Dropdown.Item>
          <Dropdown.Item onClick={() => updateFilter('filterType', 'day')}>
            Ngày hiện tại
          </Dropdown.Item>
          <Dropdown.Item onClick={() => updateFilter('filterType', 'week')}>
            Tuần hiện tại
          </Dropdown.Item>
          <Dropdown.Item onClick={() => updateFilter('filterType', 'month')}>
            Tháng hiện tại
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>
      <Table responsive="sm" striped bordered hover className="p-3a m-3">
        <thead>
          <tr>
            <th>#</th>
            <th>Tên Hoạt Động</th>
            <th>Địa Điểm</th>
            <th>Điều</th>
            <th>Đối tượng</th>
            <th>Khoa</th>
            <th>Ngày giờ bắt đầu</th>
            <th>Ngày giờ kết thúc</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {activities.map((a, index) => (
            <tr key={a.id} onClick={() => nav(`/activity/${a.id}`)} style={{ cursor: 'pointer' }}>
              <td>{index + 1}</td>
              <td>{a.name}</td>
              <td>{a.location}</td>
              <td>{a.article}</td>
              <td>{a.participant}</td>
              <td>{a.faculty}</td>
              <td>{a.startDateTime}</td>
              <td>{a.endDateTime}</td>
              <td>
              <Button variant="danger">Thích</Button>
              </td>
              <td>
                <Button variant="success" size="sm" onClick={() => nav(`/activity/${a.id}`)}>Chi Tiết</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default Home;
