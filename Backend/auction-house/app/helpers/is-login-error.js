import { helper } from '@ember/component/helper';

export function isLoginError(params) {
  session: Ember.inject.service();
  if(session.authToken === null || session.authToken === ''){
    return true;
  } else {
    return false;
  }
}

export default helper(isLoginError);
