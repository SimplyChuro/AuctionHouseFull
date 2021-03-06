import { helper } from '@ember/component/helper';

export function duration(params) {
  let [start, end] = params;
  var date1 = new Date();
  var date2 = new Date(end);
  var timeDiff = date2.getTime() - date1.getTime();
  if(0 > timeDiff){
    timeDiff = 0;
  }
  var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

  return diffDays;
}

export default helper(duration);
