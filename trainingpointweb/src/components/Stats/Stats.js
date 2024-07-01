import { useContext, useEffect, useRef, useState } from "react";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import { MyUserContext } from "../../configs/Contexts";
import {
  redirect,
  useLocation,
  useNavigate,
  useSearchParams,
} from "react-router-dom";
import { Chart, defaults } from "chart.js/auto";
import { Bar, Line } from "react-chartjs-2";
import { DropdownButton, Dropdown, Container, Button } from "react-bootstrap";
import Payment from "../Payment/Payment";
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import toast from "react-hot-toast";
import axios from "axios";
import { useSearchParam } from "react-use";
import { text } from "@fortawesome/fontawesome-svg-core";

const Stats = () => {
  const [activites, setActivities] = useState([]);
  const [semester, setSemester] = useState([]);
  // const [totalPoints, setTotalPoints] = useState([]);
  const [totalConfirmedPoints, setTotalConfirmedPoints] = useState(0);
  const [data, setData] = useState(null);
  const [paid, setPaid] = useState(false);
  const { user } = useContext(MyUserContext);
  const nav = useNavigate();
  const location = useLocation();
  const resultCode = useSearchParam("resultCode");
  const status = useSearchParam("status");
  const vnpResponseCode = useSearchParam("vnp_ResponseCode");
  const pdfRef = useRef(null);
  const [searchParams, setSearchParams] = useSearchParams();
  const [show, setShow] = useState(null);

  useEffect(() => {
    if (resultCode == 0 || status == 1 || vnpResponseCode == "00") {
      setPaid(true);
    }
  }, []);

  const Load = async (filters = {}) => {
    try {
      // lay ds semester
      let sem = await APIs.get(endpoints["semester-list"]);
      if (sem.status === 200) {
        setSemester(sem.data);

        // lay hk moi nhat

        const lastestSemester = sem.data[sem.data.length - 1].id;

        if (!filters.semesterId) {
          filters.semesterId = lastestSemester;
          // Update the URL search params to include the default semesterId
          const newSearchParams = new URLSearchParams(filters);
          setSearchParams(newSearchParams);
        }

        let params = new URLSearchParams(filters);
        let res = await authApi().get(
          `${endpoints["user-registration"]}?${params}`
        );

        if (res.status === 200) {
          setActivities(res.data);
          calculatePoints(res.data);
        }
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  const calculatePoints = async (activities) => {
    try {
      let article = await APIs.get(endpoints["article-list"]);
      const articleNames = article.data.map((article) => article.name);
      const confirmedPoints = articleNames.map((articleName) => {
        return activities.reduce((acc, activity) => {
          if (
            activity.activity.article === articleName &&
            activity.participated
          ) {
            return acc + activity.point;
          }
          return acc;
        }, 0);
      });
      const maxPoints = [25, 20, 25, 20, 10, 10];
      const totalPoints = articleNames.map((articleName, index) => {
        const totalPoints = activities.reduce((acc, reg) => {
          if (reg.activity.article === articleName && reg.participated) {
            return acc + reg.point;
          }
          return acc;
        }, 0);
        return Math.min(totalPoints, maxPoints[index]);
      });
      const totalConfirmedPoints = totalPoints.reduce(
        (acc, points) => acc + points,
        0
      );
      setTotalConfirmedPoints(totalConfirmedPoints);
      const registeredPoints = articleNames.map((articleName) => {
        return activities.reduce((acc, activity) => {
          if (
            activity.activity.article === articleName &&
            !activity.participated
          ) {
            return acc + activity.point;
          }
          return acc;
        }, 0);
      });
      setData({
        labels: article.data.map((r) => r.name),
        datasets: [
          {
            label: "Điểm tối đa",
            data: [25, 20, 25, 20, 10, 10],
            backgroundColor: "lightpink",
            borderColor: "rgba(255, 99, 132, 1)",
            borderWidth: 1,
          },
          {
            label: "Điểm xác nhận",
            data: confirmedPoints,
            backgroundColor: "lightgreen",
            borderColor: "rgba(54, 162, 235, 1)",
            borderWidth: 1,
          },
          {
            label: "Điểm đăng ký",
            data: registeredPoints,
            backgroundColor: "lightblue",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
          },
        ],
      });
    } catch (ex) {
      console.error(ex);
    }
  };

  const Close = () => {
    setShow(false);
  };

  const Show = () => {
    setShow(true);
  };

  const updateFilter = (key, value) => {
    searchParams.set(key, value);
    setSearchParams(searchParams);
  };

  defaults.maintainAspectRatio = true;
  defaults.responsive = true;

  useEffect(() => {
    if (user == null) {
      nav("/login", { state: { from: location } });
    }
    let filters = Object.fromEntries([...searchParams]);
    Load(filters);
  }, [user, searchParams]);

  const exportToPdf = () => {
    const input = pdfRef.current;
    html2canvas(input).then((canvas) => {
      const imgData = canvas.toDataURL("image/png");
      const pdf = new jsPDF();
      const imgProps = pdf.getImageProperties(imgData);
      const pdfWidth = pdf.internal.pageSize.getWidth();
      const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
      pdf.addImage(imgData, "PNG", 0, 0, pdfWidth, pdfHeight);
      pdf.save("thanh-tich.pdf");
      toast.success("Thanh toán thành công!");
      setPaid(false);
    });
    nav(location.pathname, { replace: true });
  };

  // const payMomo = async () => {
  //   try {
  //     const res = await axios.post("http://localhost:5000/api/momo-pay", {
  //       total: 10000,
  //       redirectUrl: "http://localhost:3000/stats"
  //     })

  //     if (res.data.payUrl) {
  //       window.location.href = res.data.payUrl;
  //     }
  //   } catch (error) {
  //     toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
  //   }
  // };

  // const payZalo = async () => {
  //   try {
  //     const res = await axios.post("http://localhost:5000/api/zalo-pay", {
  //       total: 10000,
  //       redirectUrl: "http://localhost:3000/stats"
  //     })

  //     if (res.data.payUrl) {
  //       window.location.href = res.data.payUrl;
  //     }
  //   } catch (error) {
  //     toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
  //   }
  // }

  return (
    <>
      {user != null && semester && (
        <>
          {data && (
            <>
              <Container ref={pdfRef}>
                <h1 className="text-center m-5">Thống Kê Điểm Rèn Luyện</h1>
                <Bar
                  className="mt-3 mb-3"
                  data={data}
                  options={{
                    plugins: {
                      legend: { display: true },
                      title: {
                        display: true,
                        text: `Kết quả rèn luyện: ${totalConfirmedPoints}đ`,
                        position: "bottom",
                        color: "red",
                        font: {
                          size: 20,
                          family: "Arial",
                          weight: "bold",
                          
                        },
                      },
                    },
                  }}
                />
              </Container>
              <div className="mb-2">
                <Dropdown className="d-inline mx-2 mb-3">
                  <Dropdown.Toggle variant="success">Học Kỳ</Dropdown.Toggle>
                  <Dropdown.Menu>
                    {semester.map((s) => (
                      <Dropdown.Item
                        key={s.id}
                        onClick={() => updateFilter("semesterId", s.id)}
                      >
                        {s.semesterName}
                      </Dropdown.Item>
                    ))}
                  </Dropdown.Menu>
                </Dropdown>
                <Button className="d-inline mx-2" onClick={() => Show()}>
                  Xuất PDF
                </Button>
                <Payment Show={show} Close={Close} setPaid={setPaid} />
              </div>

              {
                paid && (
                  <div className="alert alert-info m-3">
                    Thanh toán thành công. Bấm{" "}
                    <a style={{ cursor: "pointer" }} onClick={exportToPdf}>
                      <strong>download</strong>
                    </a>{" "}
                    để tải file PDF về.
                  </div>
                )
                // <div className="d-flex flex-row justify-content-evenly m-3">
                //   <button className="btn btn-success flex-1" onClick={payMomo}>
                //     Export To PDF Momo
                //   </button>
                //   <button className="btn btn-success flex-1" onClick={payZalo}>
                //     Export To PDF Zalo
                //   </button>
                //   <button className="btn btn-success flex-1" onClick={payVnPay}>
                //     Export To PDF VNPay
                //   </button>
                // </div>
              }
            </>
          )}
        </>
      )}
    </>
  );
};

export default Stats;
