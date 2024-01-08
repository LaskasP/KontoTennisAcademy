import { defineStore } from "pinia";

export const useCourtStore = defineStore('courts', {
  state: () => ([{
      id: 1,
      name: 'Tennis_1',
      img_url: 'sofoulakos'
    },{
      id: 2,
      name: 'Tennis_2',
      img_url: 'sofoulakos'
    },{
      id: 3,
      name: 'Padel',
      img_url: 'sofoulakos'
    }]
  ),
  getters: {

  }
})