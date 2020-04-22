import Vue from 'vue'
import Login from '../../../src/pages/Login'

describe('Login.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(Login)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('.content h2').textContent)
      .toEqual('LOGIN')
    expect(vm.$el.querySelector('button').textContent)
      .toEqual("Login")
    var passform = vm.$el.querySelectorAll("input[type='password']")
    expect(passform).toHaveLength(1)
  })
})
