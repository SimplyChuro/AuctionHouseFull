import { helper } from '@ember/component/helper';

export function isMain(params) {
  const picture = params[0];
  const currentPicture = params[1];
  const index = params[2];
  if(index === null){
    return true;
  } else {
    if(index === 0 && (currentPicture === null || currentPicture === '')){
      return true;
    } else {
      if(picture !== currentPicture){
        return false;
      } else {
        return true;
      }
    }
  }
}


export default helper(isMain);
