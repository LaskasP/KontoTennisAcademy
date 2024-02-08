<script>
import { useAuthStore } from "@/stores/modules/auth/index.js";
import BaseCard from "@/components/base/BaseCard.vue";
import BaseButton from "@/components/base/BaseButton.vue";

export default {
  name: "AuthView",
  components: { BaseButton, BaseCard },
  data() {
    return {
      email: "",
      password: "",
      firstname: "",
      lastname: "",
      username: "",
      isFormInValid: false,
      mode: "login"
    };
  },
  methods: {
    submitForm() {
      this.isFormInValid =
        this.email === "" || !this.email.includes("@") || this.password.length < 6;
      if (this.mode === "login" && !this.isFormInValid) {
        // ....
      } else if(this.mode === "signup" && !this.isFormInValid){
        useAuthStore().signup({
          email: this.email,
          password: this.password,
          firstname: this.firstname,
          lastname: this.lastname,
          username: this.username
        });
      }
    },
    switchMode() {
      if (this.mode === "login") {
        this.mode = "signup";
      } else {
        this.mode = "login";
      }
    }
  },
  computed: {
    submitButtonCaption() {
      let caption = "";
      if (this.mode === "login") {
        caption = "Login";
      } else {
        caption = "Signup";
      }
      return caption;
    },
    modeButtonCaption() {
      let caption = "";
      if (this.mode === "login") {
        caption = "Signup instead";
      } else {
        caption = "Login instead";
      }
      return caption;
    }
  }
};
</script>

<template>
  <base-card>
    <form @submit.prevent="submitForm">
      <div class="form-control">
        <label for="email">E-Mail</label>
        <input type="email" id="email" v-model.trim="email" />
      </div>
      <div class="form-control">
        <label for="password">Password</label>
        <input type="password" id="password" v-model.trim="password" />
      </div>
      <div v-if="mode === 'signup'">
        <div class="form-control">
          <label for="username">Username</label>
          <input type="text" id="username" v-model.trim="username" />
        </div>
        <div class="form-control">
          <label for="firstname">Firstname</label>
          <input type="text" id="firstname" v-model.trim="firstname" />
        </div>
        <div class="form-control">
          <label for="lastname">Lastname</label>
          <input type="text" id="lastname" v-model.trim="lastname" />
        </div>
      </div>
      <p v-if="isFormInValid">Please enter a valid email and password</p>
      <base-button>{{ submitButtonCaption }}</base-button>
      <base-button type="button" mode="flat" @click="switchMode">{{ modeButtonCaption }}</base-button>
    </form>
  </base-card>
</template>

<style scoped>
form {
  margin: 1rem;
  padding: 1rem;
}

.form-control {
  margin: 0.5rem 0;
}

label {
  font-weight: bold;
  margin-bottom: 0.5rem;
  display: block;
}

input,
textarea {
  display: block;
  width: 100%;
  font: inherit;
  border: 1px solid #ccc;
  padding: 0.15rem;
}

input:focus,
textarea:focus {
  border-color: #3d008d;
  background-color: #faf6ff;
  outline: none;
}
</style>
