import { defineStore } from "pinia";

export const useUserStore = defineStore('user', {
  state: () =>{
    return {
      userId: 0,
      firstName: 'Petros',
      lastName: 'Laskas',
      username: 'Skouna',
      profileImgURL: 'http://localhost:5000',
      roles: ['USER', 'ADMIN']
    };
  },
  getters:{
    fullName: (state) => state.firstName + state.lastName
  },
  actions:{}
})
