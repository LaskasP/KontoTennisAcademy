<template>
  <header>
    <nav>
      <div class="wrapper">
        <img
          alt="KTA logo"
          class="logo"
          height="45"
          src="@/assets/KontopoulosAcademy.png"
          width="45"
        />
        <h1>
          <RouterLink to="/">KTA</RouterLink>
        </h1>
      </div>
      <ul>
        <li>
          <base-button link mode="outline" to="/">Home</base-button>
        </li>
        <li>
          <base-button link mode="outline" to="/courts">Courts</base-button>
        </li>
        <li>
          <base-button link mode="outline" to="/reservation">Reserve</base-button>
        </li>
        <li v-if="isUserLoggedIn">
          <base-button mode="outline" @click="logout">Logout</base-button>
        </li>
      </ul>
    </nav>
  </header>
</template>

<script>
import { RouterLink } from "vue-router";
import { useAuthStore } from "@/stores/auth/auth.js";
import { mapState } from "pinia";

export default {
  name: "TheHeader",
  components: { RouterLink },
  computed: {
    ...mapState(useAuthStore, ["isUserLoggedIn"])
  },
  methods: {
    logout() {
      useAuthStore().logout();
      this.$router.replace("/");
    }
  }
};
</script>

<style scoped>
div {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

header {
  width: 100%;
  height: 5rem;
  background-color: #3d008d;
  display: flex;
  justify-content: center;
  align-items: center;
}

header a {
  text-decoration: none;
  display: inline-block;
  padding: 0.75rem 1.5rem;
  border: 1px solid transparent;
}

a:active,
a:hover,
a.router-link-active {
  border: 1px solid #f391e3;
}

h1 {
  margin: 0;
}

h1 a {
  color: white;
  margin: 0;
}

h1 a:hover,
h1 a:active,
h1 a.router-link-active {
  border-color: transparent;
}

header nav {
  width: 90%;
  margin: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

header ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

li {
  margin: 0 0.5rem;
}
</style>
