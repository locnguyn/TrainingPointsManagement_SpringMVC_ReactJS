import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./Styles.css"; // Import a CSS file for custom styles

const Home = () => {
  return (
    <Container className="home-container">
      <Row className="justify-content-center text-center">
        <Col md={8}>
          <h1 className="mt-5">Chào Mừng Đến Với Hệ Thống Quản Lý Điểm Rèn Luyện</h1>
          <p className="lead mt-3">
            Đây là hệ thống quản lý điểm rèn luyện cho sinh viên. Bạn có thể theo dõi và cập nhật các hoạt động và điểm rèn luyện của mình tại đây.
          </p>
          <div className="mt-4">
            <Link to="/activity">
              <Button variant="primary" className="me-2">
                Xem Hoạt Động
              </Button>
            </Link>
            <Link to="/report_missing">
              <Button variant="secondary">
                Báo Thiếu
              </Button>
            </Link>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Home;
