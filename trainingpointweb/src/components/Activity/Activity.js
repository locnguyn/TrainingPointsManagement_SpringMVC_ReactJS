import { useContext, useEffect, useRef, useState } from "react";
import { Button, ButtonGroup, Nav, Tab, Table, Tabs } from "react-bootstrap";
import "./styles.css";
import { authApi, endpoints } from "../../configs/APIs";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { MyDispatcherContext, MyUserContext } from "../../configs/Contexts";
import { FaMinusCircle } from "react-icons/fa";
import toast from "react-hot-toast";
import MyModal from "../Common/Modal";

const Activity = () => {
  const { user, userReport, userActivity } = useContext(MyUserContext);
  const [activites, setActivites] = useState([]);
  const [filterActivities, setFilterActivities] = useState([]);
  const nav = useNavigate();
  const location = useLocation();
  const { dispatch: dispatchActivity, dispatchReport } = useContext(MyDispatcherContext);
  const proof = useRef();
  const [previewURL, setPreviewURL] = useState(null);
  const [show, setShow] = useState(false);
  const [acPartType, setAcPartType] = useState();

  const loadActivity = async () =>{
    try{
      let res = await authApi().get(endpoints['user-registration']);

      if(res.status === 200){
        setActivites(res.data);
        setFilterActivities(res.data);
      }

    }catch(ex) {
      console.log(ex);
    }
  }

  const delRegistration = async (e, registrationId) => {
    e.preventDefault();

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
        setActivites((preActivities) =>
          preActivities.filter((a) => a.id != registrationId)
        );
      }

    } catch (ex) {
      console.error(ex);
    }
  };

  const FilterActivities = (article) => {
    setFilterActivities(activites.filter((a) => a.activity.article == article));
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

      let res = await await toast.promise(
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
      Close()

      // setReports((preReports =>{
      //   preReports.filter((r) => r.activityPartType.id != acPartTypedId);
      // }))

      dispatchReport({
        type: "update-reports",
        payload: [...userReport, res.data]
      })
    } catch (ex) {
      console.log(ex);
    }
  };

  useEffect(() => {
    if (user !== null) {
      dispatchActivity({
        type: "update-activities",
        payload: [...activites],
      });
    }
  }, [activites]);

  useEffect(() => {
    if (user === null) {
      nav("/login", { state: { from: location } });
    }
    loadActivity();
  }, [user, nav, location]);
  return (
    <>
      {user !== null && (
        <>
          <h1 className="text-center mt-3">Hoạt Động Tham Gia</h1>
          <Tabs
            defaultActiveKey="default"
            id="uncontrolled-tab-example"
            className="mb-3"
            onSelect={(k) => {
              if (k === "default") setFilterActivities(activites);
              else FilterActivities(k);
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
