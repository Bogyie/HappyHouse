<template>
  <div class="home">
    <home-view />
  </div>
</template>

<script>
// @ is an alias to /src
import HomeView from "@/components/Home.vue";

export default {
  name: "HomeView",
  components: {
    HomeView,
  },
};
</script>
