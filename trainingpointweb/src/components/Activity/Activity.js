import { useContext, useEffect, useRef, useState } from "react";
import {
  Button,
  ButtonGroup,
  Dropdown,
  Nav,
  Tab,
  Table,
  Tabs,
} from "react-bootstrap";
import "./styles.css";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import {
  Link,
  useLocation,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import { FaMinusCircle } from "react-icons/fa";
import toast from "react-hot-toast";
import MyModal from "../Common/Modal";
import { useSearchParam } from "react-use";
import { defaultPageSize } from "../../configs/configs";
import LoadMore from "../Common/LoadMore";

const Activity = () => {
  const { user } = useContext(MyUserContext);
  const [activities, setActivities] = useState([]);
  const [semester, setSemester] = useState([]);
  const [article, setArticle] = useState([]);
  const [filterActivities, setFilterActivities] = useState([]);
  const nav = useNavigate();
  const location = useLocation();
  const proof = useRef();
  const [previewURL, setPreviewURL] = useState(null);
  const [show, setShow] = useState(false);
  const [acPartType, setAcPartType] = useState();
  const [page, setPage] = useState(1);
  const [loadedFull, setLoadedFull] = useState(false);
  const [selectedSemester, setSelectedSemester] = useState();

  const loadActivity = async (page = 1) => {
    if (semester.length === 0) return;
    const params = new URLSearchParams();
    try {
      // lay hk moi nhat
      const latestSemester = semester[semester.length - 1].id;

       
      if (page) {
        params.set("page", page);
      }
      if (latestSemester) {
        params.set("semesterId", latestSemester);
      }
      if (selectedSemester) {
        params.set("semesterId", selectedSemester);
      }

      let res = await authApi().get(
        `${endpoints["user-registration"]}?${params}`
      );

      if (res.status === 200) {
        const newActivities = res.data || [];
        setActivities((prevActivities) =>
          page === 1 ? newActivities : [...prevActivities, ...newActivities]
        );
        setFilterActivities((prevFilterActivities) =>
          page === 1
            ? newActivities
            : [...prevFilterActivities, ...newActivities]
        );
        updateTotalPages(newActivities.length < defaultPageSize);
      }
    } catch (ex) {
      console.log(ex);
    }
  };

  const updateTotalPages = (noMorePages) => {
    if (noMorePages) setLoadedFull(true);
  };

  const loadMore = () => {
    setPage(page + 1);
  };

  const loadSemester = async () => {
    try {
      let sem = await APIs.get(endpoints["semester-list"]);
      let arti = await APIs.get(endpoints["article-list"]);
      setSemester(sem.data);
      setArticle(arti.data);
    } catch (ex) {
      console.error(ex);
    }
  };

  const delRegistration = async (e, registrationId) => {
    e.preventDefault();

    const confirmed = window.confirm("Bạn có chắc muốn xóa?");
    if (!confirmed) return;

    try {
      let res = await toast.promise(
        authApi().delete(endpoints["registration-delete"](registrationId)),
        {
          loading: "Loading",
          success: "Xóa thành công",
          error: "Thất bại",
        }
      );

      if (res.status === 200) {
        setFilterActivities((preActivities) =>
          preActivities.filter((a) => a.id != registrationId)
        );
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  const FilterActivities = (article) => {
    setFilterActivities(
      activities.filter((a) => a.activity.article == article)
    );
  };

  const Close = () => {
    setShow(false);
    setPreviewURL(null);
  };
  const Show = (typeId) => {
    setShow(true);
    setAcPartType(typeId);
  };

  const fileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPreviewURL(URL.createObjectURL(file));
    } else {
      setPreviewURL(null);
    }
  };

  const CreateReport = async (e, acPartTypedId, proof) => {
    e.preventDefault();

    try {
      let form = new FormData();
      form.append("acPartTypeId", acPartTypedId);
      console.log(form);
      if (proof) form.append("files", proof.current.files[0]);

      let res = await toast.promise(
        authApi().post(endpoints["create-report"], form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }),
        {
          loading: "Loading",
          success: "Báo thiếu thành công",
          error: "Thất bại",
        }
      );
      Close();
    } catch (ex) {
      console.log(ex);
    }
  };

  useEffect(() => {
    loadSemester();
  }, []);

  useEffect(() => {
    loadActivity(page);
  }, [page, selectedSemester]);

  useEffect(() => {
    if (user === null) {
      nav("/login", { state: { from: location } });
    }
    loadActivity(page);
  }, [user, nav, location, semester]);

  useEffect(() => {
    setActivities([]);
    setFilterActivities([]);
    setPage(1);
    setLoadedFull(false);
  }, [selectedSemester]);
  return (
    <>
      {user !== null && (
        <>
          <h1 className="text-center mt-5 mb-3">Hoạt Động Tham Gia</h1>
          <Dropdown className="mb-3">
            <Dropdown.Toggle variant="success">Học Kỳ</Dropdown.Toggle>
            <Dropdown.Menu>
              {semester.map((s) => (
                <Dropdown.Item
                  key={s.id}
                  onClick={() => setSelectedSemester(s.id)}
                >
                  {s.semesterName}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>
          <Tabs
            defaultActiveKey="default"
            id="uncontrolled-tab-example"
            className="mb-3"
            onSelect={(k) => {
              if (k === "default") setFilterActivities(activities);
              else FilterActivities(k);
            }}
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
              <tr>
                <th>#</th>
                <th>Tên Hoạt Động</th>
                <th>Địa Điểm</th>
                <th>Điều</th>
                <th>Đối tượng</th>
                <th>Hình thức</th>
                <th>Điểm</th>
                <th>Khoa</th>
                <th>Ngày giờ bắt đầu</th>
                <th>Ngày giờ kết thúc</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {filterActivities.map((a, index) => (
                <tr key={a.id}>
                  <td>{index + 1}</td>
                  <td>{a.activity.name}</td>
                  <td>{a.activity.location}</td>
                  <td>{a.activity.article}</td>
                  <td>{a.activity.participant}</td>
                  <td>{a.participationTypeName}</td>
                  <td>{a.point}</td>
                  <td>{a.activity.faculty}</td>
                  <td>{a.activity.startDateTime}</td>
                  <td>{a.activity.endDateTime}</td>
                  <td align="middle" style={{ border: "none" }}>
                    <ButtonGroup className="me-2" size="sm" responsive="sm">
                      <Button
                        variant="danger"
                        className="me-2 rounded"
                        onClick={(e) => delRegistration(e, a.id)}
                        disabled={a.participated === true}
                      >
                        Xóa
                      </Button>
                      <Button
                        variant="success"
                        className="me-2 rounded"
                        disabled={a.participated === true}
                        onClick={() => Show(a.acPartTypeId)}
                      >
                        Báo Thiếu
                      </Button>
                    </ButtonGroup>
                  </td>

                  {/* <td align="middle">
                    <Button
                      variant="danger"
                      onClick={(e) => delRegistration(e, a.id)}
                      disabled={a.participated === true}
                      responsive
                      size="sm"
                    >
                      Xóa
                    </Button>
                  </td>
                  <td align="middle">
                    <Button
                      variant="warning"
                      disabled={a.participated === true}
                      responsive
                      size="sm"
                    >
                      Báo Thiếu
                    </Button>
                  </td> */}
                </tr>
              ))}
            </tbody>
          </Table>
          {!loadedFull && (
            <LoadMore objName="hoạt động" handleLoadMore={loadMore} />
          )}
          <MyModal
            Show={show}
            Close={Close}
            fileChange={fileChange}
            CreateReport={CreateReport}
            previewURL={previewURL}
            proof={proof}
            acPartTypedId={acPartType}
          />
        </>
      )}
    </>
  );
};

export default Activity;
