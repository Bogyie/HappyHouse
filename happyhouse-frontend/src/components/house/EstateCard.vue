<template>
  <vue-flip :active-click="true" width="390px" height="480px">
    <template v-slot:front>
      <el-card :body-style="{ padding: '0px', }" style="width:390px;height:480px">
        <div v-bind:id="`${houseData.count}`" style="width:390px; height:400px;"></div> <!-- 로드뷰 영역 -->
        <div style="padding: 10px">
          <span>{{ houseData.name }}</span>
          <div class="bottom">
            <div class="price" v-if="houseData.deposit == 0">매매 {{ houseData.price / 10000 }}만원</div>
            <div class="price" v-else-if="houseData.rental == 0">전세 {{ houseData.deposit / 10000 }}만원</div>
            <div class="price" v-else>보증금 {{ houseData.deposit / 10000 }}만원, 월세 {{ houseData.rental / 10000 }}만원</div>
          </div>
        </div>
      </el-card>
    </template>
    <template v-slot:back>
      <el-card :body-style="{ padding: '0px' }" style="width:390px;height:480px" @dblclick="moveEstate">
        <div style="padding: 14px">
          <div class="detail">거래 일자 : {{ houseData.tradeDay }}</div>
          <div class="detail">상세 주소</div>
          <div class="roadAddress">{{ houseData.roadAddress }}</div>
          <div class="detail" v-if="houseData.deposit == 0">거래 방식 : 매매</div>
          <div class="detail" v-else>거래 방식 : 전월세</div>
          <div class="detail" v-if="houseData.deposit == 0">매매가 : {{ houseData.price / 10000 }}만원</div>
          <div class="detail" v-else-if="houseData.rental == 0">전세금 : {{ houseData.deposit / 10000 }}만원
          </div>
          <div class="detail" v-else>보증금 : {{ houseData.deposit / 10000 }}만원<br />월세 : {{ houseData.rental / 10000 }}만원
          </div>
          <div class="detail">층수 : {{ houseData.floor }}</div>
          <div class="detail">평수 : {{ houseData.area_kor }}평 ({{ houseData.area }}㎡)</div>
          <div class="detail">준공일자 : {{ houseData.buildYear }}년</div>
        </div>
        <div class="comment">
          더블 클릭 시 더 자세한 정보를 볼 수 있습니다.
        </div>
      </el-card>
    </template>
  </vue-flip>
</template>

<script>
import { VueFlip } from "vue-flip";

export default {
  components: {
    'vue-flip': VueFlip,
  },
  props: ['houseData'],
  data: () => ({
    position: {
      top: 0,
      left: 0,
      bottom: 0,
      right: 0,
      value: undefined
    },
    visible: false,
  }),
  computed: {
    triggerRef: function getBoundingClientRect() {
      return this.position.value
    }
  },
  methods: {
    roadMap() {
      kakao.maps.disableHD();
      const { lat, lon } = this.houseData;
      const roadviewContainer = document.getElementById(`${this.houseData.count}`); //로드뷰를 표시할 div
      const roadview = new kakao.maps.Roadview(roadviewContainer);

      kakao.maps.event.addListener(roadview, 'init', function () {
        const kakaoLabels = document.querySelectorAll("div[id^='_kakao_copyright_'");
        kakaoLabels.forEach(label => label.remove());

        // _box_util_
        const zoomButtons = document.querySelectorAll("div[id^='_box_util_'");
        zoomButtons.forEach(button => button.remove());
      });

      const roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체
      const position = new kakao.maps.LatLng(lat, lon);

      roadviewClient.getNearestPanoId(position, 150, function (panoId) {
        roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
      });
    },
    moveEstate() {
      // Detail의 props 로 사용
      localStorage.setItem("estateDetail", JSON.stringify(this.houseData));
      this.$router.push({ name: "EstateDetail" });
    }
  },
  mounted() {
    document.addEventListener('mousemove', (e) => {
      this.position.value = DOMRect.fromRect({
        width: 0,
        height: 0,
        x: e.clientX,
        y: e.clientY,
      });
    });
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
          this.roadMap();
        })
      })
      document.head.appendChild(script)
    } else {
      // 이미 카카오맵 API가 로딩되어 있다면 바로 지도를 생성한다.
      this.roadMap()
    }
  },
}
</script>

<style scoped>
.el-card {
  border-width: 0px;
  margin-bottom: 0px;
}

.roadAddress {
  font-size: 15px;
  padding: 10px;
  font-weight: bold;
}

.detail {
  padding: 5px;
  font-weight: bold;
}

.price {
  font-size: 13px;
  color: #999;
  float: left;
}

.bottom {
  margin-top: 20px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment {
  padding: 0;
  min-height: auto;
  font-size: 12px;
  position: absolute;
  right: 60px;
  bottom: 35px;
  background-color: rgb(240, 240, 240);
}
</style>