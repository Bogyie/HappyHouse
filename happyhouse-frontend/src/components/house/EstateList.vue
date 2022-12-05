<template>
  <el-container class="infinite-list-wrapper">
    <ul class="flex-box" v-infinite-scroll="load" :infinite-scroll-distance="600" :infinite-scroll-disabled="disabled"
      :infinite-scroll-delay="100">
      <estate-card class="estate-card infinite-list-item" :houseData="house" v-for="house in houses" :key="house">
      </estate-card>
    </ul>
    <p v-if="loading">Loading...</p>
    <p v-if="isNoMore">No more</p>
  </el-container>
</template>
  
<script>
import { houseClient } from "@/clients";
import EstateCard from "./EstateCard.vue";

export default {
  components: {
    EstateCard,
  },
  data: () =>
  ({
    page: 1,
    houses: [],
    serviceType: "apt-trade",
    localCode: 26440,
    dealYear: 2022,
    dealMonth: 5,
    items: [],
    loading: false,
    isNoMore: false,
  }),
  methods: {
    load() {
      const numOfRows = 4;
      const { page, serviceType, localCode, dealYear, dealMonth } = this;
      this.loading = true;
      houseClient.getHouses(serviceType, localCode, dealYear, dealMonth, page, numOfRows)
        .then((data) => {
          if (data?.items) {
            let count = page * numOfRows;
            const items = data.items.map(item => {
              const result = { ...item, count };
              count++;
              return result;
            });
            this.houses.push(...items);
            if (items.length < numOfRows) {
              this.isNoMore = true;
              this.loading = false;
            }
          } else {
            this.isNoMore = true;
          }
          this.loading = false;
        });

      this.page = this.page + 1;
    },
  },
  async mounted() {
    const searchQuery = localStorage.getItem("searchQuery");
    if (searchQuery) {
      const { localCode, serviceType, dealYear, dealMonth } = JSON.parse(searchQuery);
      this.localCode = localCode;
      this.serviceType = serviceType;
      this.dealYear = dealYear;
      this.dealMonth = dealMonth;
    }
    this.load();
  },
  computed: {
    disabled() {
      return this.loading || this.isNoMore;
    },
  }
}
</script>
  
<style scoped>
body {
  -ms-overflow-style: none;
}

::-webkit-scrollbar {
  display: none;
}

.flex-box {
  width: 1680px;
  display: flex;
  flex-wrap: wrap;
}

.estate-card {
  margin-right: 10px;
  margin-bottom: 10px;
}

.infinite-list-wrapper {
  overflow: auto;
  height: 90vh;
  text-align: center;
}
</style>