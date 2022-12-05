import { createWebHistory, createRouter } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home",
    component: () =>import("@/components/Home"),
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/components/user/Login"),
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/components/user/Register"),
  },
  {
    path: "/info",
    name: "Info",
    component: () => import("@/components/user/Info"),
  },
  {
    path: "/findpwd",
    name: "FindPwd",
    component: () => import("@/components/user/FindPwd"),
  },
  {
    path: "/changePwd",
    name: "ChangePwd",
    component: () => import("@/components/user/ChangePwd"),
  },
  {
    path: "/board/:id",
    name: "Board",
    component: () => import("@/components/board/BoardList"),
  },
  {
    path: "/board/detail/:id",
    name: "BoardDetail",
    component: () => import("@/components/board/article/BoardDetail"),
  },
  {
    path: "/article",
    name: "Article",
    children: [
      {
        path: 'detail/:id',
        component: () => import("@/components/board/article/BoardDetail"),
      },
      {
        path: 'modify/:id',
        component: () => import("@/components/board/article/BoardModify"),
      },
      {
        path: "write",
        name: "BoardWrite",
        component: () => import("@/components/board/article/BoardWrite"),
      },
    ]
  },
  {
    path: "/estate",
    name: "Estate",
    redirect: "/estate/list",
    children: [
      {
        path: "list",
        name: "EstateList",
        component: () => import("@/components/house/EstateList"),
      },
      {
        path: "detail",
        name: "EstateDetail",
        component: () => import("@/components/house/EstateDetail"),
      },
    ]
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;

