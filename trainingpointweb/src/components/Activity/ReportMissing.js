import { useContext, useEffect, useState } from "react";
import {
  Badge,
  Card,
  Container,
  Dropdown,
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
import APIs, { authApi, endpoints } from "../../configs/APIs";
import { defaultPageSize } from "../../configs/configs";
import LoadMore from "../Common/LoadMore";

const Report = () => {
  const { user } = useContext(MyUserContext);
  const [reports, setReports] = useState([]);
  const [semester, setSemester] = useState([]);
  const [article, setArticle] = useState([]);
  const [filterReports, setFilterReports] = useState([]);
  const nav = useNavigate();
  const location = useLocation();
  const [showModal, setShowModal] = useState(false);
  const [proof, setProof] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState("default");
  const [selectedArticle, setSelectedArticle] = useState("default");
  const [selectedSemester, setSelectedSemester] = useState();
  const [page, setPage] = useState(1);
  const [loadedFull, setLoadedFull] = useState(false);

  const LoadReports = async (page = 1) => {
    if(semester.length === 0) return ;
    const params = new URLSearchParams();
    try {
      // lay hk moi nhat
      const latestSemester = semester[semester.length - 1].id;
      if(latestSemester) params.set("semesterId", latestSemester);

      //pagination
      if(page) params.set("page", page);

      if (selectedSemester) {
        params.set("semesterId", selectedSemester);
      }
      let res = await authApi().get(`${endpoints["user-report"]}?${params}`);

      if (res.status === 200) {
        const newReports = res.data || [];
        setFilterReports((preFilterReports) =>
          page === 1 ? newReports : [...preFilterReports, ...newReports]
        );
        setReports((preReports) =>
          page === 1 ? newReports : [...preReports, ...newReports]);
        updateTotalPages(newReports.length < defaultPageSize);
      }
    } catch (ex) {
      console.log(ex);
    }
  };

  const loadCommon = async () => {
    try {
      let sem = await APIs.get(endpoints["semester-list"]);
      let ar = await APIs.get(endpoints["article-list"]);
      setArticle(ar.data);
      setSemester(sem.data);
    } catch (ex) {
      console.error(ex);
    }
  };

  //pagination
  const updateTotalPages = (noMorePages) => {
    if (noMorePages) setLoadedFull(true);
  };

  const loadMore = () => {
    setPage(page + 1);
  };

  const FilteredReports = () => {
    let filtered = reports;

    if (selectedStatus !== "default") {
      filtered = filtered.filter((r) => r.status === selectedStatus);
    }
    if (selectedArticle !== "default") {
      filtered = filtered.filter(
        (r) => r.activityPartType.activity.article === selectedArticle
      );
    }
    setFilterReports(filtered);
  };

  const Show = (image) => {
    setShowModal(true);
    setProof(image);
  };

  const Close = () => {
    setShowModal(false);
  };

  useEffect(() => {
    loadCommon();
  }, []);

  useEffect(()=>{
    LoadReports(page);
  },[page, selectedSemester])

  useEffect(() => {
    FilteredReports();
  }, [selectedArticle, selectedStatus]);

  useEffect(() => {
    if (user == null) nav("/login", { state: { from: location } });
    LoadReports(page);
    // FilteredReports();
  }, [user, semester]);

  useEffect(()=>{
    setPage(1);
    setLoadedFull(false);
    setFilterReports([]);
    setReports([]);
  }, [selectedSemester])

  return (
    <>
      {user != null && (
        <>
          <h1 className="text-center mt-4 mb-3">Báo Thiếu</h1>
          <div className="d-flex align-items-center mb-3">
            <Dropdown className="mb-3">
              <Dropdown.Toggle variant="success">Học Kỳ</Dropdown.Toggle>
              <Dropdown.Menu>
                {semester.map((s) => (
                  <Dropdown.Item key={s.id} onClick={() => setSelectedSemester(s.id)}>{s.semesterName}</Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
            <Nav
              variant="pills"
              className="mb-3"
              onSelect={(k) => setSelectedStatus(k)}
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
          </div>

          <Tabs
            defaultActiveKey="default"
            id="uncontrolled-tab-example"
            className="mb-3"
            onSelect={(k) => setSelectedArticle(k)}
          >
            <Tab eventKey="default" title="Tất cả"></Tab>
            {article &&
              article.length > 0 &&
              article.map((a) => (
                <Tab key={a.id} eventKey={a.name} title={a.name}></Tab>
              ))}
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
              {user != null && reports.length > 0 && (
                <>
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
                </>
              )}
            </tbody>
          </Table>
          {!loadedFull &&(
            <LoadMore objName="hoạt động" handleLoadMore={loadMore} />
          )}
        </>
      )}
    </>
  );
};

export default Report;
