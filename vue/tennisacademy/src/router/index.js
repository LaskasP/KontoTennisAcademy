import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/components/views/HomeView.vue";
import ReservationView from "@/components/views/ReservationView.vue";
import NotFoundView from "@/components/views/NotFoundView.vue";

const CourtsView = () => import("@/components/views/CourtsView.vue");
const ProfileView = () => import("@/components/views/ProfileView.vue");
const RegisterView = () => import("@/components/views/RegisterView.vue");

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView
    },
    {
      path: "/reservation",
      name: "reservation",
      component: ReservationView
    },
    {
      path: "/courts",
      name: "courts",
      component: CourtsView
    },
    {
      path: "/profile/:id",
      name: "profile",
      component: ProfileView
    },
    {
      path: "/register",
      name: "register",
      component: RegisterView
    },
    {
      path: "/:notFound(.*)",
      name: "notFound",
      component: NotFoundView
    }
  ]
});

export default router;
