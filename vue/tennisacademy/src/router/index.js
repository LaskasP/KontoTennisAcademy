import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/components/views/HomeView.vue";
import ReservationView from "@/components/views/ReservationView.vue";
import NotFoundView from "@/components/views/NotFoundView.vue";
import { useAuthStore } from "@/stores/auth/auth.js";

const CourtsView = () => import("@/components/views/CourtsView.vue");
const ProfileView = () => import("@/components/views/ProfileView.vue");
const AuthView = () => import("@/components/views/AuthView.vue");

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
      component: ReservationView,
      meta: { requiresAuth: true }
    },
    {
      path: "/courts",
      name: "courts",
      component: CourtsView
    },
    {
      path: "/profile/:id",
      name: "profile",
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: "/auth",
      name: "auth",
      component: AuthView,
      meta: { requiresLogout: true }
    },
    {
      path: "/:notFound(.*)",
      name: "notFound",
      component: NotFoundView
    }
  ]
});

router.beforeEach(function (to, from, next) {
  if (to.meta.requiresAuth && !useAuthStore().isUserLoggedIn) {
    next("/auth");
  } else if (to.meta.requiresLogout && useAuthStore().isUserLoggedIn) {
    next("/");
  } else {
    next();
  }
});
export default router;
