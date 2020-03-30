export const TOKEN_COOKIE_HEADER = 'token'
export const TOKEN_PREFIX = 'Bearer '
export const TOKEN_HEADER_STRING = 'authorization'

export const OWNER_ID_HEADER_STRING = 'ownerid'
export function setCookie (cname, cvalue, exdays) {
  var d = new Date()
  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000))
  var expires = 'expires=' + d.toUTCString()
  document.cookie = cname + '=' + cvalue + ';' + expires + ';path=/'
}
export function addCookie (cname, cvalue, exdays) {
  var d = new Date()
  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000))
  var expires = 'expires=' + d.toUTCString()
  document.cookie += cname + '=' + cvalue + ';' + expires + ';path=/'
}
export function getCookie (cname) {
  var name = cname + '='
  var decodedCookie = document.cookie
  var ca = decodedCookie.split(';')
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i]
    while (c.charAt(0) === ' ') {
      c = c.substring(1)
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length)
    }
  }
  return ''
}
