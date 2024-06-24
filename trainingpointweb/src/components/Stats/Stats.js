import { useContext, useEffect, useRef, useState } from "react";
import APIs, { authApi, endpoints } from "../../configs/APIs";
import { MyUserContext } from "../../configs/Contexts";
import { redirect, useLocation, useNavigate } from "react-router-dom";
import { Chart, defaults } from "chart.js/auto";
import { Bar, Line } from "react-chartjs-2";
import { DropdownButton, Dropdown } from "react-bootstrap";
import { useSearchParam } from "react-use";
import toast from "react-hot-toast";
import jsPDF from "jspdf";
import html2canvas from "html2canvas";
import axios from "axios";

const Stats = () => {
  const [activites, setActivites] = useState([]);
  const [data, setData] = useState(null);
  const [paid, setPaid] = useState(false);
  const { user } = useContext(MyUserContext);
  const nav = useNavigate();
  const location = useLocation();
  const resultCode = useSearchParam("resultCode");
  const status = useSearchParam("status");
  const vnpResponseCode = useSearchParam("vnp_ResponseCode");
  const pdfRef = useRef(null);

  useEffect(() => {
    if (resultCode == 0 || status == 1 || vnpResponseCode == "00") {
      setPaid(true);
    }
  }, [])

  const Load = async () => {
    try {
      let res = await authApi().get(endpoints["user-registration"]);
      let article = await APIs.get(endpoints["article-list"]);

      if (res.status === 200 && article.status === 200) {
        setActivites(res.data);
        const articleNames = article.data.map((article) => article.name);
        const confirmedPoints = articleNames.map((articleName) => {
          return res.data.reduce((acc, activity) => {
            if (
              activity.activity.article === articleName &&
              activity.participated
            ) {
              return acc + activity.point;
            }
            return acc;
          }, 0);
        });
        const registeredPoints = articleNames.map((articleName) => {
          return res.data.reduce((acc, activity) => {
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
              data: [25, 20, 20, 25, 10, 10],
              backgroundColor: "rgba(255, 99, 132, 0.2)",
              borderColor: "rgba(255, 99, 132, 1)",
              borderWidth: 1,
            },
            {
              label: "Điểm xác nhận",
              data: confirmedPoints,
              backgroundColor: "rgba(54, 162, 235, 0.2)",
              borderColor: "rgba(54, 162, 235, 1)",
              borderWidth: 1,
            },
            {
              label: "Điểm đăng ký",
              data: registeredPoints,
              backgroundColor: "rgba(75, 192, 192, 0.2)",
              borderColor: "rgba(75, 192, 192, 1)",
              borderWidth: 1,
            },
          ],
        });
      }
    } catch (ex) {
      console.error(ex);
    }
  };

  defaults.maintainAspectRatio = true;
  defaults.responsive = true;

  useEffect(() => {
    if (user == null) {
      nav("/login", { state: { from: location } });
    }
    Load();
  }, [user]);

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
      toast.success("Download file PDF thành công!");
      setPaid(false);
    });
    nav(location.pathname, { replace: true });
  };

  const payMomo = async () => {
    try {
      const res = await axios.post("http://localhost:5000/api/momo-pay", {
        total: 10000,
        redirectUrl: "http://localhost:3000/stats"
      })

      if (res.data.payUrl) {
        window.location.href = res.data.payUrl;
      }
    } catch (error) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
    }
  };

  const payZalo = async () => {
    try {
      const res = await axios.post("http://localhost:5000/api/zalo-pay", {
        total: 10000,
        redirectUrl: "http://localhost:3000/stats"
      })

      if (res.data.payUrl) {
        window.location.href = res.data.payUrl;
      }
    } catch (error) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
    }
  }

  const payVnPay = async () => {
    const form = new FormData();
    form.append("amount", 10000);
    form.append("orderInfo", "Thanh toán cho Điểm rèn luyện Lộc Hiếu");
    try {
      const res = await authApi().post(endpoints['vn-pay'], form);
      console.log(res);

      if (res.data) {
        window.location.href = res.data;
      }
    } catch (error) {
      toast.error("Có lỗi xảy ra, vui lòng thử lại sau");
    }
  }

  return (
    <>
      {user != null && (
        <>

          {data && (
            <>
              <div ref={pdfRef}>
                <h1 className="text-center m-3">Thống Kê Điểm Rèn Luyện</h1>
                <Bar
                  data={data}
                  options={{
                    legend: { display: false },
                    title: {
                      display: true,
                      text: "Predicted world population (millions) in 2050",
                    },
                  }}
                />
              </div>
              {paid ? <div className="alert alert-success m-4">Thanh toán thành công. Bấm <a style={{ cursor: "pointer" }} onClick={exportToPdf}><strong>download</strong></a> để tải file PDF về.</div> :
                <div className="d-flex flex-row justify-content-evenly m-3">
                  <button className="btn btn-success flex-1" onClick={payMomo}>
                    Export To PDF Momo
                  </button>
                  <button className="btn btn-success flex-1" onClick={payZalo}>
                    Export To PDF Zalo
                  </button>
                  <button className="btn btn-success flex-1" onClick={payVnPay}>
                    Export To PDF VNPay
                  </button>
                </div>}
            </>
          )}
        </>
      )}
    </>
  );
};

export default Stats;
