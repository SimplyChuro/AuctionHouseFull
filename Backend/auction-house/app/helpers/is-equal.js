import Helper from '@ember/component/helper';

export default Helper.extend({
  
  compute(params) {
    var firstValue = params[0];
    var secondValue = params[1];
    
    if(firstValue == secondValue) {
      return true;
    } else {
      return false;
    }
  }

});
