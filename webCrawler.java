	
	import java.util.*;
	import java.io.*;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	public class webCrawler {
	
		additionClass add;
		static String s;
		static int i = 0;
		static int found = 0;
		String entries[]; 
	
		private class Worker implements Runnable {
			Map<String, String> map = new HashMap<String, String>();	// For storing files/folders and respective paths
			Map<String, Map<String, String>> fileContent = new HashMap<String, Map<String, String>>();
			// For storing file, path and its content (in case of text file, html file etc) 
			private additionClass queue;
	
	
			public Worker(additionClass q) {
				queue = q;
			}
	
			public void run() {
	
				String file_Name;
				
				while ((file_Name = queue.remove()) != null) {
					File fp = new File(file_Name);
					entries =  fp.list();
					
					if (entries == null)
						continue;
					
					for(int j=0;j<entries.length;j++) {
						
						if (entries[j].compareTo(".") == 0)
							continue;
						if (entries[j].compareTo("..") == 0)
							continue;
	
						String fn = file_Name + "\\" + entries[j];
						map.put(entries[j],fn);		
						
						
					}
				}
	
				for (Map.Entry<String, String> i : map.entrySet()) {
					String key = i.getKey();
					String values = i.getValue();
					System.out.println("File name = " + key);
					System.out.println("File path = " + values);
					System.out.println();
					}
			
				for (Map.Entry<String, String> i : map.entrySet()) {
					String key = i.getKey();
					String values = i.getValue();
						if(key.contains(".txt") || key.contains(".html") || key.contains(".xml") || key.contains(".pdf")  ) { 
							// if text file, html file, xml file or pdf file
							List<String> tok = readFromFile(values);		//Saving content of file in list tok
							StringBuilder sb = new StringBuilder();	
							for (String s : tok) {							//Converting list to string for storage in our map
								sb.append(s);
								sb.append(" ");
							}
	
							fileContent.put(key, new HashMap<String, String>());    //Store file name first
							fileContent.get(key).put(values, sb.toString());   		//Store file path and file content then
							
							String content = fileContent.get(key).get(values);		//Get content of a file
							if(content.contains(s)) {
								System.out.println("Your input string " + s + "  is present in file:  " + key);
								System.out.println("File name = " + key);
								System.out.println("File path = " + values);
								System.out.println();
							}
	
						}
						if(key.contains(s)) {						
							//Condition to check whether a file containing given input is present in directory or not
							System.out.println("FILE is present in the directory:" + s);
							System.out.println("File name = " + key);
							System.out.println("File path = " + values);
							System.out.println();
						}
				}
				
			}
	
		}
	
		public webCrawler() {
			add = new additionClass();
		}
	
		public Worker createWorker() {
			return new Worker(add);
		}
	
		
	
		public List<String> readFromFile(String file) {			//Read function to read and store the contents of file 
			List<String> tokens = new ArrayList<String>();		//For storing file content
			BufferedReader buffread = null;
			try {
	
				String cLine;
				buffread = new BufferedReader(new FileReader(file));
				while ((cLine = buffread.readLine()) != null) {
					tokens.add(cLine);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (buffread != null)buffread.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
	
			return tokens;
		}
	
	
		public void processDirectory(String directory) {
	
			try{
	
				File file = new File(directory);
				if (file.isDirectory()) {
					String entries[] = file.list();
					if (entries != null)
						add.add(directory);
	
					for (String entry : entries) {
						String subdir;
						if (entry.compareTo(".") == 0)
							continue;
						if (entry.compareTo("..") == 0)
							continue;
						if (directory.endsWith("\\")) {
							subdir = directory+entry;
	
	
						}
						else {
							subdir = directory+"\\"+entry;
	
							processDirectory(subdir);
	
						}
	
					}
				}}catch(Exception e){}
		}
	
		public static void myfunc() {
	
			webCrawler myObj = new webCrawler();	
			Scanner in = new Scanner(System.in);	
			System.out.println("SEARCH: ");
			s = in.nextLine();
			System.out.println("You searched for: " +s);
	
	
			//THREAD PART
		
			ArrayList<Thread> tr = new ArrayList<Thread>(5);
			for (int i = 0; i < 5; i++) {
				Thread t = new Thread(myObj.createWorker());
				tr.add(t);
				t.start();
			}

			myObj.processDirectory("D:\\NUST/6th Semester/Artificial Intelligence/LABS");
			myObj.add.finish();
	
			for (int i = 0; i < 5; i++){
				try {
					tr.get(i).join();
				} catch (Exception e) {};
			}
		}
	}
