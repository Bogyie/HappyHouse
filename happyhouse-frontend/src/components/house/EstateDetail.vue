<template>
  <el-row>
    <el-col :span="10" :offset="0">
      <el-card class="detail" :body-style="{ padding: '3px' }">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="로드뷰" name="roadview">
            <div id="roadview" style="width:742px; height:400px;"></div>
          </el-tab-pane>
          <el-tab-pane label="차트" name="chart">
            <div id="googleTrend" style="width:742px; height:450px;" />
          </el-tab-pane>
        </el-tabs>
        <div style="padding: 14px">
          <div class="title">아파트명 :
            <span class="content">{{ name }}</span>
          </div>
          <div class="title">
            거래일자 :
            <span class="content">{{ tradeDay }}</span>
          </div>
          <div class="title">
            평수 :
            <span class="content">{{ area_kor }}평 ({{ area }})㎡</span>
          </div>
          <div class="title">
            매매가 :
            <span class="content">{{ price / 10000 }}만원</span>
          </div>
          <div v-if="rental != 0" class="title">
            보증금 :
            <span class="content">{{ rental / 10000 }}만원</span>
          </div>
          <div v-if="deposit != 0" class="title">
            월세 :
            <span class="content">{{ deposit / 10000 }}만원</span>
          </div>
          <div class="title">
            상세주소 :
            <span class="content">{{ roadAddress }}</span>
          </div>
          <div class="title">
            층수 :
            <span class="content">{{ floor }}</span>
          </div>
          <div class="title">
            준공년도 :
            <span class="content">{{ buildYear }}</span>
          </div>
        </div>
        <div class="bottom">
          <el-button class="button" @click="moveList">목록으로</el-button>
        </div>
      </el-card>
    </el-col>
    <el-col :span="13" :offset="1">
      <el-card class="map" :body-style="{ padding: '15px' }">
        <el-col :span="24" id="map" style="height: 685px  ;"></el-col>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  data: () => ({
    buildYear: undefined,
    tradeDay: undefined,
    address: undefined,
    floor: undefined,
    name: undefined,
    area: undefined,
    area_kor: undefined,
    deposit: undefined,
    rental: undefined,
    price: undefined,
    roadAddress: undefined,
    jibunAddress: undefined,
    englishAdress: undefined,
    lon: undefined,
    lat: undefined,
    activeName: "roadView",
  }),
  methods: {
    moveList() {
      this.$router.push({ name: "EstateList" });
    },
    initMap() {
      const container = document.getElementById('map')

      const options = {
        // 처음 지도의 위치를 lat, lng(위도, 경도) 값으로 설정한다.
        center: new kakao.maps.LatLng(this.lat, this.lon),
        level: 3
      }

      this.map = new kakao.maps.Map(container, options);

      // 마커가 표시될 위치입니다 
      var markerPosition = new kakao.maps.LatLng(this.lat, this.lon);

      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
        position: markerPosition
      });

      // 마커가 지도 위에 표시되도록 설정합니다
      marker.setMap(this.map);
    },
    roadMap() {
      kakao.maps.disableHD();
      const roadviewContainer = document.getElementById("roadview"); //로드뷰를 표시할 div
      const roadview = new kakao.maps.Roadview(roadviewContainer);

      kakao.maps.event.addListener(roadview, 'init', function () {
        const kakaoLabels = document.querySelectorAll("div[id^='_kakao_copyright_'");
        kakaoLabels.forEach(label => label.remove());

        // // _box_util_
        // const zoomButtons = document.querySelectorAll("div[id^='_box_util_'");
        // zoomButtons.forEach(button => button.remove());
      });

      const roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체
      const position = new kakao.maps.LatLng(this.lat, this.lon);

      roadviewClient.getNearestPanoId(position, 200, function (panoId) {
        roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
      });
    },
    moveEstate() {
      // Detail의 props 로 사용
      localStorage.setItem("estateDetail", JSON.stringify(this.houseData));
      this.$router.push({ name: "EstateDetail" });
    },
    googleTrendReq(keyword, width = "100%", height = "423px") {
      const src = `{"comparisonItem":[{"keyword":"${keyword}","geo":"KR","time":"today 12-m"}],"category":0,"property":""}&amp;tz=-540&amp;forceMobileMode=false&amp;isPreviewMode=true&amp;eq=q=%EB%B6%80%EB%8F%99%EC%82%B0&geo=KR&date=today 12-m&amp;hl=ko"width="${width}"frameborder="0"scrolling="0"style="border-radius:2px;box-shadow:rgba(0,0,0,0.12)0px0px2px0px,rgba(0,0,0,0.24)0px2px2px0px;height:${height};`
      return encodeURIComponent(src);
    }
  },
  mounted() {
    const propsData = JSON.parse(localStorage.getItem("estateDetail"));

    this.buildYear = propsData.buildYear;
    this.tradeDay = propsData.tradeDay;
    this.address = propsData.address;
    this.floor = propsData.floor;
    this.name = propsData.name;
    this.area = propsData.area;
    this.area_kor = propsData.area_kor;
    this.deposit = propsData.deposit;
    this.rental = propsData.rental;
    this.price = propsData.price;
    this.roadAddress = propsData.roadAddress;
    this.jibunAddress = propsData.jibunAddress;
    this.englishAdress = propsData.englishAdress;
    this.lon = propsData.lon;
    this.lat = propsData.lat;

    if (!window.kakao || !window.kakao.maps) {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src =
        '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=d8ec99fda26237f91a46e44d476cd530&libraries=clusterer,drawing,services'
      /* eslint를 사용한다면 kakao 변수가 선언되지 않았다고
       * 오류를 내기 때문에 아래 줄과 같이 전역변수임을
       * 알려주어야 한다. */
      /* global kakao */
      script.addEventListener('load', () => {
        kakao.maps.load(() => {
          // 카카오맵 API가 로딩이 완료된 후 지도의 기본적인 세팅을 시작해야 한다.
          this.initMap()
          this.roadMap()
        })
      })
      document.head.appendChild(script)
    } else {
      // 이미 카카오맵 API가 로딩되어 있다면 바로 지도를 생성한다.
      this.initMap()
      this.roadMap()
    }

    const googleTrendElement = document.getElementById("googleTrend");

    let index = 0;
    let search = this.roadAddress;
    for (const word of this.roadAddress.split(' ')) {
      if (word.indexOf('로') >= 0) {
        search = this.roadAddress.split(' ')[index - 1];
        break;
      }
      index++;
    }
    console.log(search);

    const keyword = encodeURIComponent(search);
    googleTrendElement.innerHTML = `<iframe id="trends-widget-4" title="trends-widget-4" src="https://trends.google.co.kr/trends/embed/explore/TIMESERIES?req=%7B%22comparisonItem%22%3A%5B%7B%22keyword%22%3A%22${keyword}%22%2C%22geo%22%3A%22KR%22%2C%22time%22%3A%22today%2012-m%22%7D%5D%2C%22category%22%3A0%2C%22property%22%3A%22%22%7D&amp;tz=-540&amp;forceMobileMode=false&amp;isPreviewMode=true&amp;eq=q%3D%25EB%25B6%2580%25EB%258F%2599%25EC%2582%25B0%26geo%3DKR%26date%3Dtoday%2012-m&amp;hl=ko" width="100%" frameborder="0" scrolling="0" style="border-radius: 2px; box-shadow: rgba(0, 0, 0, 0.12) 0px 0px 2px 0px, rgba(0, 0, 0, 0.24) 0px 2px 2px 0px; height: 423px;"></iframe>`;
  }
}
</script>

<style scoped>
.map {
  border-radius: 4px
}

#map {
  border-radius: 4px
}

.detail {
  margin-bottom: 30px;
  width: 750px;
  height: 800px;
}

.title {
  padding-bottom: 10px;
  font-weight: bold;
}

.content {
  margin-left: 5px;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  float: right;
}

.button {
  margin: -30px 10px 10px 10px;
}
</style>