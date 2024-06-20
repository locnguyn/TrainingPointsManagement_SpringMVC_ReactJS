import { useContext, useEffect, useState } from "react";
import {
  Badge,
  Card,
  Container,
  Image,
  Nav,
  Navbar,
  Tab,
  Table,
  Tabs,
} from "react-bootstrap";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import "./styles.css";
import ProofModal from "../Common/ProofModal";
import toast from "react-hot-toast";
import { authApi, endpoints } from "../../configs/APIs";

const Report = () => {
  const [key, setKey] = useState("home");
  const { user, userReport } = useContext(MyUserContext);
  const [reports, setReports] = useState([]);
  const [filterReports, setFilterReports] = useState([]);
  const nav = useNavigate();
  const location = useLocation();
  const [showModal, setShowModal] = useState(false);
  const [proof, setProof] = useState(null);
  const [statusFilter, setStatusFilter] = useState("default");
  const [articleFilter, setArticleFilter] = useState("default");

  const applyFilters = (status, article) => {
    let filtered = reports;
    if (status !== "default") {
      filtered = filtered.filter((r) => r.status === status);
    }
    if (article !== "default") {
      filtered = filtered.filter((r) => r.activityPartType.activity.article === article);
    }
    setFilterReports(filtered);
  };

  const FilterReportsByArticle = (article) => {
    setFilterReports(
      reports.filter((r) => r.activityPartType.activity.article === article)
    );
    applyFilters(statusFilter, article);
  };

  const FilterReportsByStatus = (s) => {
    setFilterReports(reports.filter((r) => r.status === s));
    applyFilters(s, articleFilter);
  };

  const LoadReports = async () => {
    try {
      let res = await authApi().get(endpoints["user-report"]);

      if (res.status === 200) {
        setFilterReports(res.data);
        setReports(res.data);
      }
    } catch (ex) {
      console.log(ex);
    }
  };

  const Show = (image) => {
    setShowModal(true);
    setProof(image);
  };

  const Close = () => {
    setShowModal(false);
  };

  useEffect(() => {
    if (user == null) nav("/login", { state: { from: location } });
    LoadReports();
  }, [user]);

  return (
    <>
      {user != null && reports.length > 0 && (
        <>
          <h1 className="text-center mt-3">Báo Thiếu</h1>

          <Nav
            variant="pills"
            className="mb-3"
            onSelect={(k) => {
              if (k === "default") setFilterReports(reports);
              else FilterReportsByStatus(k);
            }}
          >
            <Nav.Item className="me-2">
              <Nav.Link eventKey="default">
                <Badge pill bg="primary">
                  Tất cả
                </Badge>
              </Nav.Link>
            </Nav.Item>
            <Nav.Item className="me-2">
              <Nav.Link eventKey="Confirmed">
                <Badge pill bg="success">
                  Đã duyệt
                </Badge>
              </Nav.Link>
            </Nav.Item>
            <Nav.Item className="me-2">
              <Nav.Link eventKey="Pending">
                <Badge pill bg="warning">
                  Chờ duyệt
                </Badge>
              </Nav.Link>
            </Nav.Item>
            <Nav.Item className="me-2">
              <Nav.Link eventKey="Rejected">
                <Badge pill bg="danger">
                  Từ chối
                </Badge>
              </Nav.Link>
            </Nav.Item>
          </Nav>

          <Tabs
            defaultActiveKey="default"
            id="uncontrolled-tab-example"
            className="mb-3"
            onSelect={(k) => {
              if (k === "default") setFilterReports(reports);
              else FilterReportsByArticle(k);
            }}
          >
            <Tab eventKey="default" title="Tất cả"></Tab>
            <Tab eventKey="Điều 1" title="Điều 1"></Tab>
            <Tab eventKey="Điều 2" title="Điều 2"></Tab>
            <Tab eventKey="Điều 3" title="Điều 3"></Tab>
            <Tab eventKey="Điều 4" title="Điều 4"></Tab>
            <Tab eventKey="Điều 5" title="Điều 5"></Tab>
            <Tab eventKey="Điều 6" title="Điều 6"></Tab>
          </Tabs>
          <Table responsive="sm" striped bordered hover className="p-3a m-3">
            <thead>
              <tr align="middle">
                <th>#</th>
                <th>Tên Hoạt Động</th>
                <th>Điều</th>
                <th>Hình thức</th>
                <th>Điểm</th>
                <th>Khoa</th>
                <th>Ngày tạo</th>
                <th>Trạng thái</th>
                <th>Minh chứng</th>
              </tr>
            </thead>
            <tbody>
              {filterReports.map((r, index) => (
                <tr key={r.id} align="middle">
                  <td>{index + 1}</td>
                  <td>{r.activityPartType.activity.name}</td>
                  <td>{r.activityPartType.activity.article}</td>
                  <td>{r.activityPartType.participationType}</td>
                  <td>{r.activityPartType.point}</td>
                  <td>{r.activityPartType.activity.faculty}</td>
                  <td>{r.reportDate}</td>
                  <td>
                    <Badge
                      pill
                      bg={
                        r.status === "Confirmed"
                          ? "success"
                          : r.status === "Rejected"
                          ? "danger"
                          : "warning"
                      }
                    >
                      {r.status === "Confirmed"
                        ? "Đã duyệt"
                        : r.status === "Pending"
                        ? "Chờ duyệt"
                        : "Từ chối"}
                    </Badge>
                  </td>
                  <td>
                    <Image
                      src={r.proof}
                      width={50}
                      onClick={() => Show(r.proof)}
                    />
                  </td>
                </tr>
              ))}
              <ProofModal Show={showModal} Close={Close} proof={proof} />
            </tbody>
          </Table>
        </>
      )}
    </>
  );
};

export default Report;
