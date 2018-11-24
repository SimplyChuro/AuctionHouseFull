import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  session: Ember.inject.service(),
  
  compute(params) {
    let session = this.get('session');
    const user = params[0];
    if(user === session.userID) {
      return true;
    } else {
      return false;
    }
  }
});

