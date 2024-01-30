<script>
export default {
  name: "UserLogin",
  data(){
    return {
      email: '',
      password: '',
      isFormInValid: false,
      mode: 'login'
    }
  },
  methods: {
    submitForm(){
      this.isFormInValid = false;
      if (this.email === '' || !this.email.includes('@') || this.password.length < 6){
        this.isFormInValid = true;
      }else{

      }
    },
    switchMode(){
      if (this.mode === 'login'){
        this.mode = 'signup';
      }else {
        this.mode = 'login';
      }
    }
  },
  computed: {
    submitButtonCaption() {
      let caption = '';
      if (this.mode === 'login'){
        caption = 'Login';
      }else{
        caption = 'Signup'
      }
      return caption;
    },
    modeButtonCaption() {
      let caption = '';
      if (this.mode === 'login'){
        caption = 'Signup instead';
      }else{
        caption = 'Login instead'
      }
      return caption;
    }
  }
};
</script>

<template>
    <div class ="base-card">
      <form @submit.prevent="submitForm">
    <div class = "form-control">
      <label for="email">E-Mail</label>
      <input type = "email" id="email" v-model.trim = "email"/>
    </div>
    <div class = "form-control">
      <label for="password">Password</label>
      <input type = "password" id="password" v-model.trim = "password"/>
    </div>
        <p v-if="isFormInValid">Please enter a valid email and password</p>
    <button>{{submitButtonCaption}}</button>
    <button type = "button" mode ="flat" @click = "switchMode">{{modeButtonCaption}}</button>
      </form>
    </div>
</template>

<style scoped>

</style>