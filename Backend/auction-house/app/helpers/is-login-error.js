import { helper } from '@ember/component/helper';

export function isLoginError(params) {
  session: Ember.inject.service();
  if(this.get('session').authToken === null || this.get('session').authToken === ''){
    return true;
  } else {
    return false;
  }
}

export default helper(isLoginError);
