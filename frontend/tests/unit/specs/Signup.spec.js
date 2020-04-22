import Vue from 'vue'
import SignUp from '../../../src/pages/SignUp'

describe('Signup.vue', () => {
  it('should render correct contents', () => {
    const Constructor = Vue.extend(SignUp)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('.content h2').textContent)
      .toEqual('SIGN UP')
    expect(vm.$el.querySelector('button').textContent)
      .toEqual("Sign Up")
    var passform = vm.$el.querySelectorAll("input[type='password']")
    expect(passform).toHaveLength(2)
  })
})
