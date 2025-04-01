/**
 * API Service for interacting with the backend Ollama service
 */

// API base URL
const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Generate code or text using the Ollama Qwen2.5 Coder LLM
 * @param {string} prompt - The prompt to send to the LLM
 * @returns {Promise<Object>} - Promise containing the response
 */
export async function generateCode(prompt) {
  try {
    const response = await fetch(`${API_BASE_URL}/assistant/generate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ prompt }),
    });
    
    if (!response.ok) {
      throw new Error(`Server responded with ${response.status}: ${response.statusText}`);
    }
    
    return await response.json();
  } catch (error) {
    console.error('Error calling generateCode API:', error);
    throw error;
  }
} 