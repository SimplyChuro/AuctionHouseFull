import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  session: Ember.inject.service(),
  
  compute(params) {
    let session = this.get('session');
    return session.authToken;
  }
});
