<script>
import { useAuthStore } from "@/stores/auth/auth.js";
import BaseCard from "@/components/base/BaseCard.vue";
import BaseButton from "@/components/base/BaseButton.vue";
import BaseDialog from "@/components/base/BaseDialog.vue";

export default {
  name: "AuthView",
  components: { BaseDialog, BaseButton, BaseCard },
  data() {
    return {
      authStore: useAuthStore(),
      email: "",
      password: "",
      firstname: "",
      lastname: "",
      username: "",
      isFormInValid: false,
      mode: "login",
      isLoading: false,
      error: null
    };
  },
  methods: {
    async submitForm() {
      this.isFormInValid =
        (this.email === "" || !this.email.includes("@") || this.password.length < 6) &&
        this.mode === "signup";
      try {
        if (this.mode === "login") {
          this.isLoading = true;
          await this.authStore.login({
            username: this.username,
            password: this.password
          });
        } else if (this.mode === "signup" && !this.isFormInValid) {
          this.isLoading = true;
          await this.authStore.signup({
            email: this.email,
            password: this.password,
            firstname: this.firstname,
            lastname: this.lastname,
            username: this.username
          });
        }
      } catch (err) {
        this.error = err.message;
      }
      this.isLoading = false;
    },
    switchMode() {
      if (this.mode === "login") {
        this.mode = "signup";
      } else {
        this.mode = "login";
      }
    },
    handleError() {
      this.error = null;
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
  <div>
    <base-dialog :show="!!error" title="Error occurred while authenticating" @close="handleError">
      <p>{{ error }}</p>
    </base-dialog>
    <base-dialog :show="isLoading" fixed title="Loading..."></base-dialog>
    <base-card>
      <form @submit.prevent="submitForm">
        <div class="form-control">
          <label for="username">Username</label>
          <input id="username" v-model.trim="username" type="text" />
        </div>
        <div class="form-control">
          <label for="password">Password</label>
          <input id="password" v-model.trim="password" type="password" />
        </div>
        <div v-if="mode === 'signup'">
          <div class="form-control">
            <label for="email">E-Mail</label>
            <input id="email" v-model.trim="email" type="email" />
          </div>
          <div class="form-control">
            <label for="firstname">Firstname</label>
            <input id="firstname" v-model.trim="firstname" type="text" />
          </div>
          <div class="form-control">
            <label for="lastname">Lastname</label>
            <input id="lastname" v-model.trim="lastname" type="text" />
          </div>
        </div>
        <p v-if="isFormInValid">Please enter a valid email and password</p>
        <base-button>{{ submitButtonCaption }}</base-button>
        <base-button mode="flat" type="button" @click="switchMode"
          >{{ modeButtonCaption }}
        </base-button>
      </form>
    </base-card>
  </div>
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
