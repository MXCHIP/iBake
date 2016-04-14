package com.mxchip.utils;

public class EasyLinkTXTRecordUtil {
	public static String setDeviceName(String inDeviceName) {
		try {
			if (null != inDeviceName && !"".equals(inDeviceName)
					&& inDeviceName.contains("#")) {
				return inDeviceName.substring(0, inDeviceName.indexOf("#"));
			} else {
				return inDeviceName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inDeviceName;
	}

	// 锟斤拷冒锟脚碉拷
	public static String setDeviceMac(String inDeviceMac) {
		try {
			if (null != inDeviceMac && !"".equals(inDeviceMac)
					&& inDeviceMac.contains("MAC")) {
				return inDeviceMac.substring(inDeviceMac.indexOf("MAC=") + 4,
						inDeviceMac.indexOf("MAC=") + 21);
			} else {
				return inDeviceMac;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inDeviceMac;
	}

	// 没锟斤拷冒锟脚碉拷
	public static String setDeviceMacWithoutMark(String inDeviceMac) {
		try {
			if (null != inDeviceMac && !"".equals(inDeviceMac)
					&& inDeviceMac.contains("MAC")) {
				return deleteMacMark(inDeviceMac.substring(
						inDeviceMac.indexOf("MAC=") + 4,
						inDeviceMac.indexOf("MAC=") + 21));
			} else {
				return inDeviceMac;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inDeviceMac;
	}

	/**
	 * delete the macMark of the card
	 * @param inDeviceMac
	 * @return
	 */
	public static String deleteMacMark(String inDeviceMac) {
		try {
			if (null != inDeviceMac && !"".equals(inDeviceMac)
					&& inDeviceMac.contains(":")) {
				return inDeviceMac.replaceAll(":", "");
			} else {
				return inDeviceMac;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inDeviceMac;
	}

	public static String setDeviceIP(String inDeviceIP) {
		try {
			if (null != inDeviceIP && !"".equals(inDeviceIP)
					&& inDeviceIP.contains("/")) {
				return inDeviceIP.substring(1);
			} else {
				return inDeviceIP;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inDeviceIP;
	}

	/**
	 * set the hardware Reversion of the card
	 * @param strInputString
	 * @return
	 */
	public static String setHardwareRev(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Hardware Rev")
					&& strInputString.contains("MICO OS Rev")) {
				int startIndex = strInputString.indexOf("Hardware Rev") + 13;
				int endIndex = strInputString.indexOf("MICO OS Rev");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	/**
	 * set the Manufacturer of card
	 * @param strInputString
	 * @return
	 */
	public static String setManufacturer(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Manufacturer=")
					&& strInputString.contains("Seed=")) {
				int startIndex = strInputString.indexOf("Manufacturer=") + 13;
				int endIndex = strInputString.indexOf("Seed=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	/**
	 * set the mac address of card
	 * @param strInputString
	 * @return
	 */
	public static String setMac(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("MAC=")
					&& strInputString.contains("Firmware Rev=")) {
				int startIndex = strInputString.indexOf("MAC=") + 4;
				int endIndex = strInputString.indexOf("Firmware Rev=") - 1;
				return strInputString.substring(startIndex, endIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * check if the card was binded
	 * @param strInputString
	 * @return
	 */
	public static String setBinding(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Binding=")
					&& strInputString.contains("Firmware Rev=")) {
				int startIndex = strInputString.indexOf("Binding=") + 8;
				int endIndex = strInputString.indexOf("Firmware Rev=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
//				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * Find FogProductId from the input String
	 * @param strInputString
	 * @return
	 */
	public static String setFogProductID(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("FogProductId=")
					&& strInputString.contains("IsEasylinkOK=")) {
				int startIndex = strInputString.indexOf("FogProductId=") + 13;
				int endIndex = strInputString.indexOf("IsEasylinkOK=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * Find IsEasylinkOK from the input String
	 * @param strInputString
	 * @return
	 */
	public static String setIsEasylinkOK(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("IsEasylinkOK=")
					&& strInputString.contains("IsHaveSuperUser=")) {
				int startIndex = strInputString.indexOf("IsEasylinkOK=") + 13;
				int endIndex = strInputString.indexOf("IsHaveSuperUser=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * Find IsHaveSuperUser from the input String
	 * @param strInputString
	 * @return
	 */
	public static String setIsHaveSuperUser(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("IsHaveSuperUser=")
					&& strInputString.contains("RemainingUserNumber=")) {
				int startIndex = strInputString.indexOf("IsHaveSuperUser=") + 16;
				int endIndex = strInputString.indexOf("RemainingUserNumber=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * Find RemainingUserNumber from the input String
	 * @param strInputString
	 * @return
	 */
	public static String setRemainingUserNumber(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("RemainingUserNumber=")
					&& strInputString.contains("Hardware Rev=")) {
				int startIndex = strInputString.indexOf("RemainingUserNumber=") + 20;
				int endIndex = strInputString.indexOf("Hardware Rev=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * Get the hardwareid pf MicoSoft's cloud 
	 * @param strInputString
	 * @return
	 */
	public static String sethardwareID(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Hardware ID=")
					&& strInputString.contains("Firmware Rev=")) {
				int startIndex = strInputString.indexOf("Hardware ID=") + 12;
				int endIndex = strInputString.indexOf("Firmware Rev=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return "0";
//				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	public static String setSeed(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Seed=")) {
				int startIndex = strInputString.indexOf("Seed=") + 5;
				// int endIndex = strInputString.indexOf("Firmware Rev=");
				return strInputString.substring(startIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	/**
	 * set the model of the card
	 * @param strInputString
	 * @return
	 */
	public static String setModel(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Model=")
					&& strInputString.contains("Protocol=")) {
				int startIndex = strInputString.indexOf("Model=") + 6;
				int endIndex = strInputString.indexOf("Protocol=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	/**
	 * setProtocol of the cardndIndex-1
	 * @param strInputString
	 * @return
	 */
	public static String setProtocol(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Protocol=")
					&& strInputString.contains("Manufacturer=")) {
				int startIndex = strInputString.indexOf("Protocol=") + 9;
				int endIndex = strInputString.indexOf("Manufacturer=");
				return strInputString.substring(startIndex, endIndex-1);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	/**
	 * set MiCO OS's reversion(endIndex-1)
	 * @param strInputString
	 * @return
	 */
	public static String setMICOOSRev(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("MICO OS Rev=")
					&& strInputString.contains("Model=")) {
				int startIndex = strInputString.indexOf("MICO OS Rev=") + 12;
				int endIndex = strInputString.indexOf("Model=");
				return strInputString.substring(startIndex, endIndex-1);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}

	public static String setFirmwareRev(String strInputString) {
		try {
			if (null != strInputString && !"".equals(strInputString)
					&& strInputString.contains("Firmware Rev=")
					&& strInputString.contains("Hardware Rev=")) {
				int startIndex = strInputString.indexOf("Firmware Rev=") + 13;
				int endIndex = strInputString.indexOf("Hardware Rev=");
				return strInputString.substring(startIndex, endIndex);
			} else {
				return strInputString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strInputString;
	}
	
	/**
	 * get all info
	 * @param strInputString
	 * @return
	 */
	public static String setAllInfo(String strInputString) {
		strInputString.replaceAll("#",",");
		return strInputString;
	}

}
